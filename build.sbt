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
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.1.3",
      "com.typesafe.akka" %% "akka-stream" % "2.5.14",
      "de.heikoseeberger" %% "akka-http-circe" % "1.21.0",
      "com.h2database" % "h2" % "1.4.197",
      "com.typesafe.slick" %% "slick" % "3.2.3",
      "org.joda" % "joda-money" % "0.12",
      "eu.timepit" %% "refined" % "0.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.specs2" %% "specs2-core" % "4.2.0" % "it; test"
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % "0.9.3")
  )
  .dependsOn(migrations, codegen)
  .aggregate(migrations, codegen)

lazy val migrations = project in file("migrations")

lazy val codegen = (project in file("codegen"))
  .settings(
    libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.3"
  )
  .dependsOn(migrations)
  .aggregate(migrations)
