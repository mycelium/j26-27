name := "funsets"

scalaVersion := "2.13.16"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
)

fork := true

javaOptions += "-Xmx2G"

Test / parallelExecution := false
