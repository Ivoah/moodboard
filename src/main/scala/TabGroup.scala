package net.ivoah.moodmapper

import scalatags.Text.all.*

case class TabGroup(tabs: Seq[(String, Frag)]) extends CustomComponent {
  def render: Frag = div(id:=scoped("tab-group"), `class`:="tab-group",
    div(`class`:="tab-bar",
      for (((title, _), i) <- tabs.zipWithIndex) yield div(
        id:=scoped(s"tab-$i"),
        `class`:="tab",
        zIndex:=(tabs.length - i),
        if (i == 0) attr("active"):="" else frag(),
        onclick:=
          s"""$$("#${scoped("tab-group")} .tab-body").hide();
             |$$("#${scoped("tab-group")} .tab").removeAttr("active");
             |$$("#${scoped(s"tab-$i")}").attr("active", "");
             |$$("#${scoped(s"tab-body-$i")}").show();
             |""".stripMargin,
        title
      )
    ),
    for (((_, body), i) <- tabs.zipWithIndex) yield div(
      id:=scoped(s"tab-body-$i"), `class`:="tab-body",
      display:=(if (i == 0) "block" else "none"),
      body
    )
  )
}
