package net.ivoah.moodboard

import java.time.*
import java.time.format.{DateTimeFormatter, TextStyle}
import java.util.Locale
import scalatags.Text.all.*

val DAYS_OF_WEEK = Seq(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)

object Calendar {
  private val fullformat = DateTimeFormatter.ofPattern("MMMM yyyy")
}

case class Calendar(year: Int, month: Int) {
  private val date = YearMonth.of(year, month)

  private val days = (-DAYS_OF_WEEK.indexOf(date.atDay(1).getDayOfWeek) + 1 until 0) ++ (0 to date.lengthOfMonth())

  def render(): Frag = div(textAlign:="center", border:="1px solid", margin:="5px",
    date.format(Calendar.fullformat),
    table(
      thead(tr(
        for (day <- DAYS_OF_WEEK) yield th(day.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.US))
      )),
      tbody(
        for (week <- days.grouped(7).toSeq) yield tr(
          for (day <- week) yield td(
            if (day > 0) day
            else ""
          )
        )
      )
    )
  )
}
