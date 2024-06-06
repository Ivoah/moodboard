package net.ivoah.moodmapper

import scala.util.Random

case class Colormap(id: Int, name: String, owner: Int, private val colors: Seq[Color]) {
  def color(i: Int): Color = colors.lift(i).getOrElse(Color.PURPLE)
  def randomColor: Color = colors(Random.nextInt(colors.length))
}
