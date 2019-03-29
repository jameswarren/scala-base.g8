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

publish := {}

def projectDef(name: String, directory: String): Project = {
  Project(name, file(directory))
    .settings(
      (Compiling.compileSettings
        ++ Testing.testSettings
        ++ Assembly.assemblySettings
        ++ Plugins.pluginSettings): _*
    )
}

lazy val core = projectDef("$name$-core", "core")

onLoad in Global ~= (_ andThen ("project $name$-core" :: _))
