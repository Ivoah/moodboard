package net.ivoah.moodmapper

import scalatags.Text.all.*

import java.time.*
import java.time.format.TextStyle
import java.util.Locale

val DAYS_OF_WEEK = Seq(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)

case class Calendar(year: Int, month: Int, entries: Seq[Entry], colormap: Colormap, modifiers: Modifier*) extends CustomComponent {
  private val date = YearMonth.of(year, month)

  private val days = (-DAYS_OF_WEEK.indexOf(date.atDay(1).getDayOfWeek) + 1 until 0) ++ (0 to date.lengthOfMonth())

  def render: Frag = div(
    `class`:="calendar",
    id:=scoped("calendar"),
    strong(date.getMonth.getDisplayName(TextStyle.FULL_STANDALONE, Locale.US)), s" ${date.getYear}",
    table(
      thead(tr(
        for (day <- DAYS_OF_WEEK) yield th(day.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US))
      )),
      tbody(
        for (week <- days.grouped(7).toSeq) yield tr(
          for (day <- week) yield {
            if (day > 0) {
              val c = entries
                .find(_.date == date.atDay(day))
                .map(_.value)
                .map(colormap.color)
                .getOrElse(Color.WHITE)
              td(day, backgroundColor:=c.toCSS, color:=c.getContrastColor.toCSS, onclick:=
                s"""const wasSelected = $$(this).hasClass("selected");
                   |$$("#${scoped("calendar")} td").removeClass("selected");
                   |if (!wasSelected) $$(this).addClass("selected");
                   |""".stripMargin)
            } else td()
          }
        )
      )
    ),
    modifiers
  )
}
