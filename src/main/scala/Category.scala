package net.ivoah.moodmapper

import java.time.YearMonth

case class Category(id: Int, name: String, owner: Int) {
  def entries(month: YearMonth): Seq[Entry] = Database.getEntries(id, month)
}
