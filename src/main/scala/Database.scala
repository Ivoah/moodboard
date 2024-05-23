package net.ivoah.moodmapper

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

import java.sql.{Connection, DriverManager, Statement}
import scala.util.{Try, Using}

class Database(user: String, password: String) {
  private val db: Connection = DriverManager.getConnection("jdbc:mysql://ivoah.net/moodmapper?autoReconnect=true", user, password)
  private val argon2 = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

  def isValidLogin(username: String, password: String): Option[Int] = {
    val results = Using.resource({
      db.prepareStatement("SELECT id, hash FROM user WHERE username = ?")
    }) { stmt =>
      stmt.setString(1, username)
      val results = stmt.executeQuery()
      if (results.next()) Some((results.getInt("id"), results.getString("hash")))
      else None
    }

    results.map { case (id, hash) if argon2.matches(password, hash) => id }
  }

  def registerUser(username: String, password: String): Option[Int] = {
    val hash = argon2.encode(password)

    Using.resource({
      db.prepareStatement("INSERT INTO user (username, hash) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)
    }) { stmt =>
      stmt.setString(1, username)
      stmt.setString(2, hash)
      Try {
        stmt.executeUpdate()
        val results = stmt.getGeneratedKeys
        results.getInt(1)
      }
    }.toOption
  }

  def getUsername(id: Int): String = {
    Using.resource({
      db.prepareStatement("SELECT username FROM user WHERE id = ?")
    }) { stmt =>
      stmt.setInt(1, id)
      val results = stmt.executeQuery()
      results.next()
      results.getString("username")
    }
  }

  def getCategories(id: Int): Seq[String] = {
    Using.resource({
      db.prepareStatement("SELECT name FROM category WHERE owner = ?")
    }) { stmt =>
      stmt.setInt(1, id)
      val results = stmt.executeQuery()
      val buffer = collection.mutable.Buffer[String]()
      while (results.next()) {
        buffer.append(results.getString("name"))
      }
      buffer.toSeq
    }
  }
}
