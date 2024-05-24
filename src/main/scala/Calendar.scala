package net.ivoah.moodmapper

import scalatags.Text.all.*

import java.time.*
import java.time.format.TextStyle
import java.util.Locale

val DAYS_OF_WEEK = Seq(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)

case class Calendar(year: Int, month: Int, entries: Seq[Entry], modifiers: Modifier*) extends CustomComponent {
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
            if (day > 0) {
              val color = entries
                .find(_.date == date.atDay(day))
                .map(_.value)
                .flatMap(Colormap.MOCKUP.colors.lift)
                .getOrElse(Color.BLACK)
                .toCSS
              td(day, backgroundColor:=color)
            } else td()
          }
        )
      )
    ),
    modifiers
  )
}
