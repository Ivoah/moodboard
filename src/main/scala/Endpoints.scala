package net.ivoah.moodboard

import net.ivoah.vial.*

import java.nio.file.Paths

object Endpoints {
  private def authenticated(request: Request, response: Response): Response = {
    if (request.cookies.contains(Cookie("logged_in", "true"))) response
    else Response.Redirect("/login")
  }

  private val root = Router {
    case ("GET", "/", request) =>
      authenticated(request, Response(Templates.root()))

    case (_, path, _) if path.endsWith("/") => Response.Redirect(path.stripSuffix("/"))
  }

  private val authentication = Router {
    case ("GET", "/login", request) => Response(Templates.login())
    case ("POST", "/login", request) =>
      println(request)
      if (request.form("username") == "user" && request.form("password") == "pass") {
        Response.Redirect("/").set_cookie(Cookie("logged_in", "true"))
      } else {
        Response.Redirect("/login")
      }
  }

  private val static = Router {
    case ("GET", s"/static/$file", _) =>
      Response.forFile(Paths.get(s"static/$file"))
  }

  val router: Router = root
    ++ authentication
    ++ static
}
