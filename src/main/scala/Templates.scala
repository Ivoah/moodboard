package net.ivoah.moodmapper

import scalatags.Text.all.*
import scalatags.Text.tags2.title

object Templates {
  private val doctype = "<!DOCTYPE html>\n"

  private def _head(_title: String) = head(
    title(_title),
    link(rel := "icon", href := "/static/favicon.png"),
    link(rel := "stylesheet", href := "/static/style.css"),
    script(src:="https://code.jquery.com/jquery-3.7.1.min.js")
  )

  def root(id: String, categories: Seq[String]): String = doctype + html(
    _head("Moodmapper"),
    body(
      div(display:="flex", justifyContent:="space-between", h3(s"Hello $id"), a(href:="/logout", "logout")),
      TabGroup(for (category <- categories) yield category -> frag(
        Calendar(2024, 5),
        div(maxWidth:="400px", margin:="auto",
          div(display:="flex", flexDirection:="row", flexWrap:="wrap", justifyContent:="space-between",
            for (i <- 0 until 10) yield div(display:="flex", flexDirection:="column", textAlign:="center",
              div(display:="flex", justifyContent:="center",
                div(backgroundColor:=Colormap.MOCKUP.colors(i).toCSS, width:="25px", height:="25px", borderRadius:="50%")
              ),
              strong(i + 1, margin:="10px")
            )
          )
        )
      ))
    )
  )

  def login(): String = doctype + html(
    _head("Login"),
    body(
      h3("Login"),
      form(method:="post",
        input(`type`:="text", name:="username"), br(),
        input(`type`:="password", name:="password"), br(),
        button("login")
      )
    )
  )

  def register(): String = doctype + html(
    _head("Register"),
    body(
      h3("Register"),
      form(method:="post",
        input(`type`:="text", name:="username"), br(),
        input(`type`:="password", name:="password"), br(),
        button("register")
      )
    )
  )
}
