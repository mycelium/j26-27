error id: file:///C:/Users/arllr/j26-27/tasks/term-1/3/build.sbt:
file:///C:/Users/arllr/j26-27/tasks/term-1/3/build.sbt
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -parallelExecution.
	 -parallelExecution#
	 -parallelExecution().
	 -scala/Predef.parallelExecution.
	 -scala/Predef.parallelExecution#
	 -scala/Predef.parallelExecution().
offset: 221
uri: file:///C:/Users/arllr/j26-27/tasks/term-1/3/build.sbt
text:
```scala
name := "funsets"

scalaVersion := "2.12.20"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-optimise",
  "-Yinline-warnings"
)

fork := true

javaOptions += "-Xmx2G"

Test / parallelExecution@@ := false
```


#### Short summary: 

empty definition using pc, found symbol in pc: 