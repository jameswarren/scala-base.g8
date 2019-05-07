import sbt._
import sbt.Keys._

object Testing {
  def testLibraries(scope: String): Seq[ModuleID] = {
    val prefixes = Seq(
      "org.scalacheck" %% "scalacheck" % "1.14.0",
      "org.scalatest" %% "scalatest" % "3.0.5"
    )
    prefixes map { _ % scope }
  }

  val testSettings = List(
    libraryDependencies ++= testLibraries("test"),
    testOptions += Tests.Argument("-oD")
  )
}
