lazy val root = (project in file("."))
  .settings(
    name := "offer-kata",
    organization := "org.vincibean",
    version := "0.1",
    scalaVersion := "2.12.6",
    scalafmtOnCompile := true,
    libraryDependencies ++= Seq(
      "com.h2database" % "h2" % "1.4.197",
      "com.typesafe.slick" %% "slick" % "3.2.3",
      "org.joda" % "joda-money" % "0.12",
      "eu.timepit" %% "refined" % "0.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    )
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
