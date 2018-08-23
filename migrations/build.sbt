import Dependencies.migrationsDependencies

enablePlugins(FlywayPlugin)

libraryDependencies ++= migrationsDependencies

flywayUrl := "jdbc:h2:file:./target/OFFERS_KATA"

flywayUser := "SA"

flywayPassword := ""

flywayLocations := Seq("classpath:db/migrations")
