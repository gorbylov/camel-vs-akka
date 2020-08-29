import sbt._

object Dependencies {
  val `camel-version` = "3.4.3"
  val camel         = "org.apache.camel" % "camel-core"       % `camel-version`
  val `camel-rest`  = "org.apache.camel" % "camel-rest"       % `camel-version`
  val `camel-netty` = "org.apache.camel" % "camel-netty-http" % `camel-version`

  val `akka-stream`         = "com.typesafe.akka" %% "akka-stream"          % "2.5.31"
  val `akka-http`           = "com.typesafe.akka" %% "akka-http"            % "10.2.0"

  val scalatest = "org.scalatest" %% "scalatest" % "3.1.1"
}
