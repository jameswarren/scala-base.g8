import scala.io.Source
import scala.util.Try

name := "$name$"

organization in ThisBuild := "com.jw"

version in ThisBuild := Try { Source.fromFile("VERSION").getLines.next }.getOrElse("unknown")

isSnapshot in ThisBuild := !sys.env.getOrElse("SBT_RELEASE", "false").toBoolean

version in ThisBuild := {
  val v = (version in ThisBuild).value
  val s = (isSnapshot in ThisBuild).value
  if (s) { v + "-SNAPSHOT" } else v
}

publish := {}

def testLibraries(scope: String): Seq[ModuleID] = {
  val prefixes = Seq(
    "org.scalacheck" %% "scalacheck" % "1.13.4",
    "org.scalatest" %% "scalatest" % "3.0.3",
    "org.pegdown" % "pegdown" % "1.6.0",
    "org.mockito" % "mockito-core" % "2.2.9"
  )
  prefixes map { _ % scope }
}

def jwProject(name: String, directory: String): Project = {

  val mergeSuffixes = List(".RSA", ".xsd", ".dtd", ".properties")

  val compileSettings = List(
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
  )

  val testSettings = List(
    libraryDependencies ++= testLibraries("test"),
    test in assembly := {},
    testOptions += Tests.Argument("-oD")
  )

  val ammoniteSettings = List(
    libraryDependencies += "com.lihaoyi" % "ammonite" % "1.0.2" % "test" cross CrossVersion.full,
    sourceGenerators in Test += Def.task {
      val file = (sourceManaged in Test).value / "amm.scala"
      IO.write(file, """object amm extends App { ammonite.Main().run() }""")
      Seq(file)
    }.taskValue
  )

  val assemblySettings = List(
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case PathList(xs @ _*) if mergeSuffixes.exists(xs.last.endsWith) => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )

  val publishSettings = List(
    publishMavenStyle := true
  )

  val scalastyleSettings = List(
    scalastyleFailOnError := true,
    compile in Compile := {
      (scalastyle in Compile toTask "").value
      (compile in Compile).value
    }
  )

  Project(name, file(directory))
    .settings(
      (compileSettings
        ++ testSettings
        ++ ammoniteSettings
        ++ assemblySettings
        ++ publishSettings
        ++ scalastyleSettings
        ++ LicenseChecker.settings):_*
    )
}

lazy val root = jwProject("$name$", "root").cross

lazy val $name$ = root("$scalaVersion$")
