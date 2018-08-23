import Dependencies.rootDependencies

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    parallelExecution in IntegrationTest := false,
    name := "offer-kata",
    organization := "org.vincibean",
    version := "0.1",
    scalaVersion := "2.12.6",
    scalacOptions ++= Seq("-Yrangepos"),
    scalafmtOnCompile := true,
    libraryDependencies ++= rootDependencies
  )
  .dependsOn(migrations, codegen)
  .aggregate(migrations, codegen)

lazy val migrations = project in file("migrations")

lazy val codegen = (project in file("codegen"))
  .dependsOn(migrations)
  .aggregate(migrations)
