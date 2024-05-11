package net.ivoah.moodboard

import scalatags.Text.all.*
import scalatags.Text.tags2.title

object Templates {
  private val doctype = "<!DOCTYPE html>\n"

  private def _head(_title: String) = head(
    title(_title),
    link(rel := "icon", href := "/static/favicon.png"),
    link(rel := "stylesheet", href := "/static/style.css"),
    link(rel := "stylesheet", href := "/people.css")
  )

  def root(): String = doctype + html(
    _head("Moodboard"),
    body(
      div(display:="flex", flexWrap:="wrap",
        for (i <- 0 until 4) yield Calendar(2024, 5).render()
      )
    )
  )
}
