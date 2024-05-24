package net.ivoah.moodmapper

import scalatags.Text.Frag

import java.util.UUID

given Conversion[CustomComponent, Frag] = _.render

trait CustomComponent {
  private val uuid = UUID.randomUUID()
  protected def scoped(name: String) = s"$name-$uuid"

  def render: Frag
}
