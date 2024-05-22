package net.ivoah.moodboard

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

import java.sql.{Connection, DriverManager}
import scala.util.{Try, Using}

class Database(user: String, password: String) {
  private val db: Connection = DriverManager.getConnection("jdbc:mysql://ivoah.net/moodmapper?autoReconnect=true", user, password)
  private val argon2 = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

  def isValidLogin(username: String, password: String): Boolean = {
    val hash = Using.resource({
      db.prepareStatement("SELECT hash FROM users WHERE username = ?")
    }) { stmt =>
      stmt.setString(1, username)
      val results = stmt.executeQuery()
      if (results.next()) Option(results.getString("hash"))
      else None
    }

    hash.exists(argon2.matches(password, _))
  }

  def registerUser(username: String, password: String): Boolean = {
    val hash = argon2.encode(password)

    Using.resource({
      db.prepareStatement("INSERT INTO users (username, hash) VALUES (?, ?)")
    }) { stmt =>
      stmt.setString(1, username)
      stmt.setString(2, hash)
      Try(stmt.executeUpdate())
    }.isSuccess
  }
}
