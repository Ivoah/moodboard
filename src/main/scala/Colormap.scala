package net.ivoah.moodmapper

import scala.util.Random

case class Color(r: Double, g: Double, b: Double) {
  def toCSS: String = s"rgb(${r*255}, ${g*255}, ${b*255})"
}

class Colormap(val colors: Seq[Color]) {
  def randomColor: Color = colors(Random.nextInt(colors.length))
}

object Colormap {
  val GREYS: Colormap = Colormap((1 to 10).map(c => Color(c/10.0, c/10.0, c/10.0)))
  val MOCKUP: Colormap = Colormap(Seq(
    Color(59/255.0, 68/255.0, 46/255.0),
    Color(83/255.0, 95/255.0, 65/255.0),
    Color(105/255.0, 119/255.0, 82/255.0),
    Color(126/255.0, 145/255.0, 99/255.0),
    Color(148/255.0, 170/255.0, 116/255.0),
    Color(170/255.0, 195/255.0, 134/255.0),
    Color(193/255.0, 221/255.0, 151/255.0),
    Color(214/255.0, 236/255.0, 181/255.0),
    Color(227/255.0, 248/255.0, 197/255.0),
    Color(232/255.0, 254/255.0, 198/255.0)
  ))
}
