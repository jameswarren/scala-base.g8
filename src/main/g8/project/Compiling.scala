import sbt._
import sbt.Keys._

object Compiling {
  val compileSettings = List(
    resolvers ++= Seq(Resolver.sonatypeRepo("public"), Resolver.sonatypeRepo("releases")),
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:higherKinds"),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0")    
  )
}
