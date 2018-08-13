enablePlugins(CodegenPlugin)

slickCodegenDatabaseUrl := "jdbc:h2:file:./target/OFFERS_KATA"

slickCodegenDatabaseUser := "SA"

slickCodegenDatabasePassword := ""

slickCodegenDriver := slick.jdbc.H2Profile

slickCodegenJdbcDriver := "org.h2.Driver"

slickCodegenOutputPackage := "org.vincibean.kata.offers.dao"

slickCodegenOutputDir := (sourceManaged in Compile).value

libraryDependencies += "com.h2database" % "h2" % "1.4.197"
