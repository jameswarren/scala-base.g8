import sbt._
import sbt.Keys._

import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtOnCompile
import org.scalastyle.sbt.ScalastylePlugin.autoImport.{scalastyle, scalastyleFailOnError}

object Plugins {
  val ammoniteSettings = List(
    libraryDependencies += "com.lihaoyi" % "ammonite" % "1.6.6" % "test" cross CrossVersion.full,
    sourceGenerators in Test += Def.task {
      val file = (sourceManaged in Test).value / "amm.scala"
      IO.write(file, """object amm extends App { ammonite.Main().run() }""")
      Seq(file)
    }.taskValue
  )

  val scalastyleSettings = List(
    scalastyleFailOnError := true,
    compile in Compile := {
      (scalastyle in Compile toTask "").value
      (compile in Compile).value
    }
  )

  val scalafmtSettings = List(
    scalafmtOnCompile := true
  )

  val pluginSettings = ammoniteSettings ++ scalastyleSettings ++ scalafmtSettings
}
