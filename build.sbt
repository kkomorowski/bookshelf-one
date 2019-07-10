name := "bookshelf-one"

version := "0.1"

scalaVersion := "2.12.8"

val tapir = Seq("tapir-core", 
                "tapir-json-circe", 
                "tapir-openapi-circe-yaml",
                "tapir-openapi-docs",
                "tapir-http4s-server")
              .map("com.softwaremill.tapir" %% _ % "0.8.8")

libraryDependencies ++= tapir
libraryDependencies += "org.webjars" % "swagger-ui" % "3.22.2"
libraryDependencies += "org.http4s" %% "http4s-dsl" % "0.20.3"