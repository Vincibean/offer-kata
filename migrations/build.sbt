enablePlugins(FlywayPlugin)

libraryDependencies += "com.h2database" % "h2" % "1.4.197"

flywayUrl := "jdbc:h2:file:./target/OFFERS_KATA"

flywayUser := "SA"

flywayPassword := ""

flywayLocations := Seq("classpath:db/migrations")
