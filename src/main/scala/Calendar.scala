package net.ivoah.moodboard

import java.time.*
import java.time.format.{DateTimeFormatter, TextStyle}
import java.util.Locale
import scalatags.Text.all.*
import scalatags.text.Builder

val DAYS_OF_WEEK = Seq(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)

object Calendar {
  private val fullformat = DateTimeFormatter.ofPattern("MMMM yyyy")
}

case class Calendar(year: Int, month: Int, modifiers: Modifier*) extends CustomComponent {
  private val date = YearMonth.of(year, month)

  private val days = (-DAYS_OF_WEEK.indexOf(date.atDay(1).getDayOfWeek) + 1 until 0) ++ (0 to date.lengthOfMonth())

  def render: Frag = div(
    `class`:="calendar",
    strong(date.getMonth.getDisplayName(TextStyle.FULL_STANDALONE, Locale.US)), s" ${date.getYear}",
    table(
      thead(tr(
        for (day <- DAYS_OF_WEEK) yield th(day.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US))
      )),
      tbody(
        for (week <- days.grouped(7).toSeq) yield tr(
          for (day <- week) yield {
            if (day > 0) td(day, backgroundColor:=Colormap.MOCKUP.randomColor.toCSS)
            else td()
          }
        )
      )
    ),
    modifiers
  )
}
