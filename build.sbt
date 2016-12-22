import de.heikoseeberger.sbtheader.license.Apache2_0
import de.heikoseeberger.sbtheader.CommentStyleMapping._

lazy val root = (project in file("."))
  .settings(
    organization := "com.lunaryorn",
    name := "scalacheck-money",
    inThisBuild(
      List(
        scalaVersion := "2.11.8",
        // Build settings
        scalacOptions ++= Seq(
          // Code encoding
          "-encoding",
          "UTF-8",
          // Build for Java 8
          "-target:jvm-1.8",
          // Deprecation warnings
          "-deprecation",
          // Warnings about features that should be imported explicitly
          "-feature",
          // Enable additional warnings about assumptions in the generated code
          "-unchecked",
          // Recommended additional warnings
          "-Xlint",
          // Warn when argument list is modified to match receiver
          "-Ywarn-adapted-args",
          // Warn about dead code
          "-Ywarn-dead-code",
          // Warn about inaccessible types in signatures
          "-Ywarn-inaccessible",
          // Warn when non-nullary overrides a nullary (def foo() over def foo)
          "-Ywarn-nullary-override",
          // Warn when numerics are unintentionally widened
          "-Ywarn-numeric-widen",
          // Fail compilation on warnings
          "-Xfatal-warnings"
        ),
        // Macro tooling (for simulacrum and others)
        addCompilerPlugin(
          "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
        libraryDependencies ++= Seq(
          "org.typelevel" %% "cats" % "0.8.1",
          "com.github.mpilquist" %% "simulacrum" % "0.10.0",
          "com.chuusai" %% "shapeless" % "2.3.2",
          "org.scalatest" %% "scalatest" % "3.0.1" % "test"
        ),
        // License headers
        headers := createFrom(Apache2_0, "2016", "Sebastian Wiesner")
      ))
  )
  .enablePlugins(AutomateHeaderPlugin)
