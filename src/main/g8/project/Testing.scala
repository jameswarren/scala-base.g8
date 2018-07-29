import sbt._
import sbt.Keys._

object Testing {
  def testLibraries(scope: String): Seq[ModuleID] = {
    val prefixes = Seq(
      "org.scalacheck" %% "scalacheck" % "1.13.4",
      "org.scalatest" %% "scalatest" % "3.0.5",
      "org.pegdown" % "pegdown" % "1.6.0",
      "org.mockito" % "mockito-core" % "2.2.9"
    )
    prefixes map { _ % scope }
  }

  val testSettings = List(
    libraryDependencies ++= testLibraries("test"),
    testOptions += Tests.Argument("-oD")
  )
}