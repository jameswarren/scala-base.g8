import sbt._
import sbt.Keys._

object Compiling {
  val compileSettings = List(
    resolvers ++= Seq(Resolver.sonatypeRepo("public")),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
  )
}