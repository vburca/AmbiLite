organization  := "me.owlcode"

version       := "0.1"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.bintrayRepo("vburca", "maven")

libraryDependencies ++= Seq(
    "com.github.finagle" %% "finch-core" % "0.10.0",
    "com.github.finagle" %% "finch-circe" % "0.10.0",
    "org.specs2" %% "specs2-core" % "2.3.11" % "test"
  )

libraryDependencies += "me.owlcode" %% "scala-lirc" % "0.1.1"
