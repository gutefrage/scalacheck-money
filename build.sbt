import de.heikoseeberger.sbtheader.license.Apache2_0
import de.heikoseeberger.sbtheader.CommentStyleMapping._
import ReleaseTransformations._

// Commands to run on Travis CI
val validateCommands = List(
  "clean",
  "checkHeaders",
  "compile",
  "test:compile",
  "scalafmtTest",
  "test",
  "doc"
)
// Do-it-all build alias for Travis CI
addCommandAlias("validate", validateCommands.mkString(";", ";", ""))

// Publish and release configuration
// Additional release step to update the version number in the README
lazy val updateVersionInReadme: ReleaseStep = { st: State =>
  val newVersion = Project.extract(st).get(version)

  val pattern = "\"net.gutefrage\" %% \"scalacheck-money\" % \"([^\"]+)\"".r
  val readme = file("README.md")
  val content = IO.read(readme)
  pattern.findFirstMatchIn(content).foreach { m =>
    IO.write(readme, m.before(1) + newVersion + m.after(1))
  }

  Seq("git", "add", readme.getAbsolutePath) !! st.log
  st
}

lazy val root = (project in file("."))
  .settings(
    // Build metadata for this project
    name := "scalacheck-money",
    startYear := Some(2016),
    description := "scalacheck generators and arbitraries for JSR 354 monetary types",
    homepage := Some(url(s"https://github.com/gutefrage/scalacheck-money")),
    licenses += "Apache-2.0" -> url(
      "http://www.apache.org/licenses/LICENSE-2.0"),
    pomExtra :=
      <developers>
        <developer>
          <id>lunaryorn</id>
          <name>Sebastian Wiesner</name>
          <url>http://www.lunaryorn.com</url>
        </developer>
        <developer>
          <id>muuki88</id>
          <name>Nepomuk Seiler</name>
          <url>https://github.com/muuki88/</url>
        </developer>
      </developers>,
    // Scaladex publishing
    scaladexKeywords in Scaladex := Seq("scalacheck"),
    // License headers
    headers := createFrom(Apache2_0, "2016-2017", "gutefrage.net GmbH"),
    // Release settings: Publish maven style, sign our releases, and define the release steps
    publishMavenStyle := true,
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    releaseTagComment := s"scalacheck-money ${(version in ThisBuild).value}",
    releaseCommitMessage := s"Bump version to ${(version in ThisBuild).value}",
    releaseCrossBuild := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runTest,
      setReleaseVersion,
      updateVersionInReadme,
      commitReleaseVersion,
      tagRelease,
      publishArtifacts,
      releaseStepTask(publish in Scaladex),
      setNextVersion,
      commitNextVersion,
      pushChanges,
      releaseStepCommand("sonatypeRelease")
    ),
    // Build settings for this specific project
    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.13.4",
      "javax.money" % "money-api" % "1.0.1",
      "org.scalatest" %% "scalatest" % "3.0.1" % Test,
      "org.javamoney" % "moneta" % "1.1" % Test
    ),
    // Settings for this project and all its subprojects.
    //
    // Note: We specify Scala versions in .travis.yml and let the sbt-travisci plugin handle the rest, so you'll not
    // find crossScalaVersions here.
    inThisBuild(
      List(
        // General build information
        organization := "net.gutefrage",
        scmInfo := Some(ScmInfo(
          url("https://github.com/gutefrage/scalacheck-money"),
          "scm:git:https://github.com/gutefrage/scalacheck-money.git",
          Some(s"scm:git:git@github.com:gutefrage/scalacheck-money.git")
        )),
        // Credentials for Travis CI, see
        // http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
        credentials ++= (for {
          username <- Option(System.getenv().get("SONATYPE_USERNAME"))
          password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
        } yield
          Credentials("Sonatype Nexus Repository Manager",
                      "oss.sonatype.org",
                      username,
                      password)).toSeq,
        // Build settings
        scalacOptions ++= Seq(
          // Code encoding
          "-encoding",
          "UTF-8",
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
        )
      )
    )
  )
  .enablePlugins(AutomateHeaderPlugin)
