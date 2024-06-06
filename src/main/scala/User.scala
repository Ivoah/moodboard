package net.ivoah.moodmapper

case class User(id: Int, username: String, hash: String) {
  lazy val categories: Seq[Category] = Database.getCategories(id)
//  lazy val colormaps: Seq[Colormap] = Database.getColormaps(id)
}
