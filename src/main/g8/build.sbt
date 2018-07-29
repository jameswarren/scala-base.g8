import scala.io.Source
import scala.util.Try

name := "$name$"

organization in ThisBuild := "$organization$"

scalaVersion := "$scalaVersion$"

isSnapshot in ThisBuild := !sys.env.getOrElse("SBT_RELEASE", "false").toBoolean

version in ThisBuild := {
  val v = Try { Source.fromFile("VERSION").getLines.next }.getOrElse("unknown")
  val s = (isSnapshot in ThisBuild).value
  if (s) { v + "-SNAPSHOT" } else v
}

scalafmtOnCompile in ThisBuild := true

scalafmtFailTest in ThisBuild := false

publish := {}

def jwProject(name: String, directory: String): Project = {
  Project(name, file(directory))
    .settings(
      (Compiling.compileSettings
        ++ Testing.testSettings
        ++ Plugins.pluginSettings): _*
    )
}

lazy val core = jwProject("$name$-core", "root")

onLoad in Global ~= (_ andThen ("project $name$-core" :: _))
