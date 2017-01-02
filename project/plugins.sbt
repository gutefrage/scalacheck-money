// Code formatting
addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.5.2-RC1")

// Copyright headers
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "1.6.0")

// API mappings for scaladoc
addSbtPlugin(
  "com.thoughtworks.sbt-api-mappings" % "sbt-api-mappings" % "1.0.0")

// Plugins for releases
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.3")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")
