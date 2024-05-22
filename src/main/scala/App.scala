package net.ivoah.moodmapper

import net.ivoah.vial.*

import java.nio.file.Paths

case class App(db: Database) {

  private val sessionManager = SessionManager("/login", "/")

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
      if (db.isValidLogin(request.form("username"), request.form("password"))) {
        sessionManager.login(request.form("username"))
      } else {
        Response.Redirect("/login")
      }
    case ("GET", "/register", request) => Response(Templates.register())
    case ("POST", "/register", request) =>
      if (db.registerUser(request.form("username"), request.form("password")))
        sessionManager.login(request.form("username"))
      else
        Response.Redirect("/register")

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
