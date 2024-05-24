package net.ivoah.moodmapper

import net.ivoah.vial.*

import java.nio.file.Paths
import java.time.*

case class Moodmapper() {

  private val sessionManager = SessionManager[User]("/login", "/")

  val router: Router = Router {
    case ("GET", "/", request) =>
      sessionManager.authenticated(request) { user =>
        Response(Templates.root(user))
      }

    case (_, path, _) if path.endsWith("/") => Response.Redirect(path.stripSuffix("/"))

    case ("GET", "/login", request) => Response(Templates.login())
    case ("POST", "/login", request) =>
      Database.isValidLogin(request.form("username"), request.form("password")) match {
        case Some(id) => sessionManager.login(id)
        case None => Response.Redirect("/login")
      }
    case ("GET", "/register", request) => Response(Templates.register())
    case ("POST", "/register", request) =>
      Database.registerUser(request.form("username"), request.form("password")) match {
        case Some(user) => sessionManager.login(user)
        case None => Response.Redirect("/register")
      }
    case ("GET", "/logout", request) => sessionManager.logout(request.cookies.find(_.name == "session").map(_.value).getOrElse(""))

    case ("GET", s"/static/$file", _) =>
      Response.forFile(Paths.get(s"static/$file"))
  }
}
