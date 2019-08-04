scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// https://github.com/sbt/sbt-scalariform
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.3")

// https://github.com/sbt/sbt-pgp
addSbtPlugin("com.jsuereth"         % "sbt-pgp"              % "1.1.2")

// https://github.com/sbt/sbt-jacoco
// https://www.scala-sbt.org/sbt-jacoco/getting-started.html
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.1.0")

// https://github.com/jrudolph/sbt-dependency-graph
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")
