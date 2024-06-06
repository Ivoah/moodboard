package net.ivoah.moodmapper

case class Color(r: Double, g: Double, b: Double) {
  def toCSS: String = s"rgb(${r*255}, ${g*255}, ${b*255})"
  def getContrastColor: Color = {
    val y = (299 * r*255 + 587 * g*255 + 114 * b*255) / 1000
    if (y >= 128) Color.BLACK else Color.WHITE
  }
}

object Color {
  val WHITE: Color = Color(1, 1, 1)
  val BLACK: Color = Color(0, 0, 0)
  val PURPLE: Color = Color(0.5, 0, 0.5)

  def fromHex(hex: String): Color = {
    hex.grouped(2).map(Integer.parseInt(_, 16).toDouble).toSeq match {
      case Seq(r, g, b) => Color(r/255, g/255, b/255)
      case _ => PURPLE
    }
  }
}
