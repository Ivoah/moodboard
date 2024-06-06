package net.ivoah.moodmapper

import net.ivoah.vial.*

import scala.util.Random

case class SessionManager[T](unauthRedirect: String, loginRedirect: String) {
  private val sessions = collection.mutable.Map[String, T]()

  def authenticated(request: Request)(response: T => Response): Response = {
    request.cookies
      .find(_.name == "session")
      .map(_.value)
      .flatMap(sessions.get)
      .map(response)
      .getOrElse(Response.Redirect(unauthRedirect))
  }

  def login(id: T): Response = {
    val session = Random.alphanumeric.take(64).mkString
    sessions(session) = id
    Response.Redirect(loginRedirect)
      .withCookie(Cookie("session", session, httpOnly = Some(true)))
  }

  def logout(session: String): Response = {
    sessions.remove(session)
    Response.Redirect(loginRedirect)
      .withCookie(Cookie("session", "", maxAge = Some(0)))
  }
}
