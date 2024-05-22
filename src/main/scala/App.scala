package net.ivoah.moodboard

import net.ivoah.vial.*
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

import java.nio.file.Paths
import java.sql.Connection
import scala.util.{Try, Success, Failure, Using}

case class App(db: Connection) {

  private val sessionManager = SessionManager("/login", "/")
  private val argon2 = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()

  private val root = Router {
    case ("GET", "/", request) =>
      sessionManager.authenticated(request) { id =>
        Response(Templates.root(id))
      }

    case (_, path, _) if path.endsWith("/") => Response.Redirect(path.stripSuffix("/"))
  }

  private val authentication = Router {
    case ("GET", "/login", request) => Response(Templates.login())
    case ("POST", "/login", request) =>
      val hash = Using.resource({
        db.prepareStatement("SELECT hash FROM users WHERE username = ?")
      }) { stmt =>
        stmt.setString(1, request.form("username"))
        val results = stmt.executeQuery()
        if (results.next()) Option(results.getString("hash"))
        else None
      }

      if (hash.exists(argon2.matches(request.form("password"), _))) {
        sessionManager.login(request.form("username"))
      } else {
        Response.Redirect("/login")
      }
    case ("GET", "/register", request) => Response(Templates.register())
    case ("POST", "/register", request) =>
      val hash = argon2.encode(request.form("password"))

      Using.resource({
        db.prepareStatement("INSERT INTO users (username, hash) VALUES (?, ?)")
      }) { stmt =>
        stmt.setString(1, request.form("username"))
        stmt.setString(2, hash)
        Try(stmt.executeUpdate())
      } match {
        case Success(_) => sessionManager.login(request.form("username"))
        case Failure(_) => Response.Redirect("/register")
      }

    case ("GET", "/logout", request) => sessionManager.logout(request.cookies.find(_.name == "session").map(_.value).getOrElse(""))
  }

  private val static = Router {
    case ("GET", s"/static/$file", _) =>
      Response.forFile(Paths.get(s"static/$file"))
  }

  val router: Router = root
    ++ authentication
    ++ static
}
