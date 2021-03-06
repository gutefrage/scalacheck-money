// Code formatting
addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.5.7")

// Copyright headers
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "1.8.0")

// API mappings for scaladoc
addSbtPlugin(
  "com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "1.0.0")

// Cross-version management and CI support
addSbtPlugin("com.dwijnand" % "sbt-travisci" % "1.1.0")

// Plugins for releases
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.4")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.1")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")
addSbtPlugin("ch.epfl.scala.index" % "sbt-scaladex" % "0.1.3")
