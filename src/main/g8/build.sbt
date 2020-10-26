name := "$name$"
publish := {}

ThisBuild / organization := "$organization$"
ThisBuild / scalaVersion := "$scalaVersion$"
ThisBuild / isSnapshot := !sys.env.getOrElse("SBT_RELEASE", "false").toBoolean
ThisBuild / version := {
  val ver = IO.readLines(new File("./VERSION")).mkString
  if ((ThisBuild / isSnapshot).value) {
    s"\$ver-SNAPSHOT"
  } else ver
}

def projectDef(name: String, directory: String): Project = {
  Project(name, file(directory))
    .settings(compileSettings)
    .settings(testSettings)
    .settings(assemblySettings)
    .settings(pluginSettings)
}

lazy val compileSettings = Seq(
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint"),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:higherKinds", "-Ymacro-annotations"),
  addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.11.0" cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
)

lazy val testSettings = Seq(
  testOptions += Tests.Argument("-oD"),
  libraryDependencies ++= Seq(
    "org.scalacheck" %% "scalacheck" % "1.14.3",
    "org.scalatest"  %% "scalatest"  % "3.2.0"
  ) map { _ % Test }
)

lazy val assemblySettings = {
  val mergeSuffixes = List(".RSA", ".xsd", ".dtd", ".properties")

  List(
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", _) => MergeStrategy.discard
      case PathList(xs @ _*) if mergeSuffixes.exists(xs.last.endsWith) =>
        MergeStrategy.first
      case x: Any =>
        val oldStrategy = (assembly / assemblyMergeStrategy).value
        oldStrategy(x)
    },
    assembly / test := {} // do not require tests when assembling
  )
}

lazy val pluginSettings = {
  val ammoniteSettings = List(
    libraryDependencies += "com.lihaoyi" % "ammonite" % "2.2.0" % "test" cross CrossVersion.full,
    Test / sourceGenerators += Def.task {
      val file = (Test / sourceManaged).value / "amm.scala"
      IO.write(file, """object amm extends App { ammonite.Main().run() }""")
      Seq(file)
    }.taskValue
  )

  val scalastyleSettings = List(
    scalastyleFailOnError := true,
    Compile / compile := {
      (Compile / scalastyle toTask "").value
      (Compile / compile).value
    }
  )

  val scalafmtSettings = List(
    scalafmtOnCompile := true
  )

  ammoniteSettings ++ scalastyleSettings ++ scalafmtSettings
}

lazy val core = projectDef("$name$-core", "core")

onLoad in Global ~= (_ andThen ("project $name$-core" :: _))
