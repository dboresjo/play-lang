val scala3Version = "3.3.3"

lazy val root = project
  .in(file("."))
  .settings(
    organization := "com.boresjo",
    name := "play-lang",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.playframework" %% "play" % "3.0.5" % Compile,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test
    )
  )
