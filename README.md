Simple [Giter8](http://www.foundweekends.org/giter8/index.html) template for creating personal Scala projects. 

Create a new project via `sbt new jameswarren/scala-base.g8`

* Creates multi-module setup with initial module called `core`.
* Integrates [Ammonite](https://ammonite.io/) so that `test:run` launches an Ammonite shell with the project context
* Adds numerous sbt plugins including
  * sbt-assembly
  * sbt-dependency-graph
  * sbt-license-report
  * sbt-scalafmt
  * sbt-scoverage
  * sbt-stats
  * sbt-updates
  * scala-clippy
