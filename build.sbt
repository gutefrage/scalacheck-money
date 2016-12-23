import de.heikoseeberger.sbtheader.license.Apache2_0
import de.heikoseeberger.sbtheader.CommentStyleMapping._

lazy val root = (project in file("."))
  .settings(
    organization := "net.gutefrage",
    name := "scalacheck-money",
    // License headers
    headers := createFrom(Apache2_0, "2016", "gutefrage.net GmbH"),
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
        libraryDependencies ++= Seq(
          // Issues blocking an update to scalacheck 1.13:
          // https://github.com/scalatest/scalatest/issues/837
          "org.scalacheck" %% "scalacheck" % "1.13.4",
          "javax.money" % "money-api" % "1.0.1",
          "org.scalatest" %% "scalatest" % "3.0.1" % Test,
          "org.javamoney" % "moneta" % "1.1" % Test
        )§
      )
    )
  )
  .enablePlugins(AutomateHeaderPlugin)
