package net.ivoah.moodmapper

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

import java.sql.{Connection, DriverManager, Statement}
import java.time.*
import scala.io.Source
import scala.util.{Try, Using}

object Database {
  val connection: Connection = {
    val s"$user:$password" = Using.resource(Source.fromResource("credentials.txt"))(_.getLines().mkString("\n")): @unchecked
    DriverManager.getConnection("jdbc:mysql://ivoah.net/moodmapper?autoReconnect=true", user, password)
  }
  private val argon2 = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

  def isValidLogin(username: String, password: String): Option[User] = {
    val results = Using.resource({
      connection.prepareStatement("SELECT id, username, hash FROM user WHERE username = ?")
    }) { stmt =>
      stmt.setString(1, username)
      val results = stmt.executeQuery()
      if (results.next()) Some(User(results.getInt("id"), results.getString("username"), results.getString("hash")))
      else None
    }

    results.filter(u => argon2.matches(password, u.hash))
  }

  def registerUser(username: String, password: String): Option[User] = {
    val hash = argon2.encode(password)

    Using.resource({
      connection.prepareStatement("INSERT INTO user (username, hash) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
    }) { stmt =>
      stmt.setString(1, username)
      stmt.setString(2, hash)
      Try {
        stmt.executeUpdate()
        val results = stmt.getGeneratedKeys
        results.getInt(1)
      }
    }.toOption.map(User(_, username, hash))
  }

  def getUser(id: Int): User = {
    Using.resource({
      connection.prepareStatement("SELECT id, username, hash FROM user WHERE id = ?")
    }) { stmt =>
      stmt.setInt(1, id)
      val results = stmt.executeQuery()
      results.next()
      User(results.getInt("id"), results.getString("username"), results.getString("hash"))
    }
  }

  def getCategories(id: Int): Seq[Category] = {
    Using.resource({
      connection.prepareStatement("SELECT id, name, owner FROM category WHERE owner = ?")
    }) { stmt =>
      stmt.setInt(1, id)
      val results = stmt.executeQuery()
      val buffer = collection.mutable.Buffer[Category]()
      while (results.next()) {
        buffer.append(Category(
          results.getInt("id"),
          results.getString("name"),
          results.getInt("owner")
        ))
      }
      buffer.toSeq
    }
  }

  def getEntries(category: Int, month: YearMonth): Seq[Entry] = {
    Using.resource({
      connection.prepareStatement("SELECT date, category, value, notes FROM entry JOIN category ON entry.category = category.id WHERE category.id = ?")
    }) { stmt =>
      stmt.setInt(1, category)
      val results = stmt.executeQuery()
      val buffer = collection.mutable.Buffer[Entry]()
      while (results.next()) {
        buffer.append(Entry(
          results.getDate("date").toLocalDate,
          results.getInt("category"),
          results.getInt("value"),
          results.getString("notes")
        ))
      }
      buffer.toSeq
    }
  }
}
