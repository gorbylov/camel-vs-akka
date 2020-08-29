import Dependencies._

ThisBuild / scalaVersion     := "2.13.2"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"


lazy val root = (project in file("."))
  .settings(
    name := "camel-vs-akka",
    libraryDependencies += camel,
    libraryDependencies += `camel-rest`,
    libraryDependencies += `camel-netty`,
    libraryDependencies += `akka-stream`,
    libraryDependencies += `akka-http`,
    libraryDependencies += scalatest % Test
  )
