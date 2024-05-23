package net.ivoah.moodmapper

import net.ivoah.vial.*

import java.nio.file.Paths

case class Moodmapper(db: Database) {

  private val sessionManager = SessionManager("/login", "/")

  val router: Router = Router {
    case ("GET", "/", request) =>
      sessionManager.authenticated(request) { id =>
        Response(Templates.root(db.getUsername(id), db.getCategories(id)))
      }

    case (_, path, _) if path.endsWith("/") => Response.Redirect(path.stripSuffix("/"))

    case ("GET", "/login", request) => Response(Templates.login())
    case ("POST", "/login", request) =>
      db.isValidLogin(request.form("username"), request.form("password")) match {
        case Some(id) => sessionManager.login(id)
        case None => Response.Redirect("/login")
      }
    case ("GET", "/register", request) => Response(Templates.register())
    case ("POST", "/register", request) =>
      db.registerUser(request.form("username"), request.form("password")) match {
        case Some(id) => sessionManager.login(id)
        case None => Response.Redirect("/register")
      }
    case ("GET", "/logout", request) => sessionManager.logout(request.cookies.find(_.name == "session").map(_.value).getOrElse(""))

    case ("GET", s"/static/$file", _) =>
      Response.forFile(Paths.get(s"static/$file"))
  }
}
