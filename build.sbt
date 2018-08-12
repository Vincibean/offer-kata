lazy val root = (project in file("."))
  .settings(
    name := "offer-kata",
    organization := "org.vincibean",
    version := "0.1",
    scalaVersion := "2.12.6",
    scalafmtOnCompile := true
  )
