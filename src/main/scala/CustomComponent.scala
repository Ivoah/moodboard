package net.ivoah.moodmapper

import java.util.UUID
import scalatags.Text.Frag

given Conversion[CustomComponent, Frag] = _.render

trait CustomComponent {
  private val uuid = UUID.randomUUID()
  protected def scoped(name: String) = s"$name-$uuid"

  def render: Frag
}
