ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.1"

ThisBuild / assemblyMergeStrategy := {
  case PathList("module-info.class") => MergeStrategy.discard
  case x if x.endsWith("/module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}

lazy val root = (project in file("."))
  .settings(
    name := "moodmapper",
    idePackagePrefix := Some("net.ivoah.moodmapper"),
    libraryDependencies ++= Seq(
      "net.ivoah" %% "vial" % "0.3.3",
      "com.lihaoyi" %% "scalatags" % "0.12.0",
      "org.rogach" %% "scallop" % "5.1.0",
      "org.springframework.security" % "spring-security-crypto" % "6.3.0",
      "org.bouncycastle" % "bcprov-jdk15on" % "1.64",
      "commons-logging" % "commons-logging" % "1.3.1",
      "mysql" % "mysql-connector-java" % "8.0.28"
    ),
    assembly / assemblyOutputPath := file("moodmapper.jar")
  )
