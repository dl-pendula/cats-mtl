name := "cats-mtl"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "org.typelevel" %% "cats-mtl-core" % "0.7.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.0.0"

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)