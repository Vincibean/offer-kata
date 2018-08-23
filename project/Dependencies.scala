import sbt._

object Dependencies {

  lazy val circeVersion = "0.9.3"

  lazy val specs2Version = "4.2.0"

  lazy val akkaHttpVersion = "10.1.4"

  lazy val rootDependencies: Seq[ModuleID] = refined ++ logging ++ money ++ storage ++ slick ++ akkaHttp ++ circe ++ specs2

  lazy val codegenDependencies: Seq[ModuleID] = storage ++ slick

  lazy val migrationsDependencies: Seq[ModuleID] = storage

  lazy val refined = Seq(
    "eu.timepit" %% "refined" % "0.9.2"
  )

  lazy val logging = Seq("ch.qos.logback" % "logback-classic" % "1.2.3")

  lazy val money = Seq("org.joda" % "joda-money" % "0.12")

  lazy val storage = Seq(
    "com.h2database" % "h2" % "1.4.197"
  )

  lazy val slick = Seq(
    "com.typesafe.slick" %% "slick" % "3.2.3"
  )

  lazy val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % "2.5.14",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "de.heikoseeberger" %% "akka-http-circe" % "1.21.0"
  )

  lazy val specs2: Seq[ModuleID] = Seq(
    "org.specs2" %% "specs2-core",
    "org.specs2" %% "specs2-scalacheck"
  ).map(_ % specs2Version % "it; test")

  lazy val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeVersion)

}
