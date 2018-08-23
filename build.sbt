import Dependencies.rootDependencies
import Scalac.options

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    parallelExecution in IntegrationTest := false,
    name := "offer-kata",
    organization := "org.vincibean",
    version := "0.1",
    scalaVersion := "2.12.6",
    scalacOptions ++= Scalac.options,
    scalacOptions in (Compile, console) --= Seq("-Ywarn-unused:imports",
                                                "-Xfatal-warnings"),
    scalafmtOnCompile := true,
    libraryDependencies ++= rootDependencies
  )
  .dependsOn(migrations, codegen)
  .aggregate(migrations, codegen)

lazy val migrations = project in file("migrations")

lazy val codegen = (project in file("codegen"))
  .dependsOn(migrations)
  .aggregate(migrations)
