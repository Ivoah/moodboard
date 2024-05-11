package net.ivoah.moodboard

import net.ivoah.vial.*

import java.nio.file.Paths

object Endpoints {
  private val root = Router {
    case ("GET", "/", request) =>
      Response(Templates.root())

    case (_, path, _) if path.endsWith("/") => Response.Redirect(path.stripSuffix("/"))

  }
  
  private val static = Router {
    case ("GET", s"/static/$file", _) =>
      Response.forFile(Paths.get(s"static/$file"))
  }
  
  val router: Router = root
    ++ static
}
