name := "stream-processing"

version := "0.1"

scalaVersion := "3.1.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.3.4",
  "co.fs2" %% "fs2-core" % "3.2.4",
  "co.fs2" %% "fs2-io" % "3.2.4"
)
