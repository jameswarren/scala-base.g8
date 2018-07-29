import sbt.Keys._
import sbtassembly.AssemblyPlugin.autoImport._

object Assembly {
  val assemblySettings = {
    val mergeSuffixes = List(".RSA", ".xsd", ".dtd", ".properties")
    List(
      assemblyMergeStrategy in assembly := {
        case PathList("META-INF", _) => MergeStrategy.discard
        case PathList(xs @ _*) if mergeSuffixes.exists(xs.last.endsWith) =>
          MergeStrategy.first
        case x: Any =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },
      test in assembly := {}
    )
  }
}
