package net.ivoah.moodmapper

import net.ivoah.vial.*

import scala.util.Random

case class SessionManager(unauthRedirect: String, loginRedirect: String) {
  private val sessions = collection.mutable.Map[String, String]()

  def authenticated(request: Request)(response: String => Response): Response = {
    request.cookies
      .find(_.name == "session")
      .map(_.value)
      .flatMap(sessions.get)
      .map(response)
      .getOrElse(Response.Redirect(unauthRedirect))
  }

  def login(id: String): Response = {
    val session = Random.alphanumeric.take(64).mkString
    sessions(session) = id
    Response.Redirect(loginRedirect).set_cookie(Cookie("session", session))
  }

  def logout(session: String): Response = {
    sessions.remove(session)
    Response.Redirect(loginRedirect)
  }
}
