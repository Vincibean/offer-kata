import Dependencies.codegenDependencies

enablePlugins(CodegenPlugin)

slickCodegenDatabaseUrl := "jdbc:h2:file:./target/OFFERS_KATA"

slickCodegenDatabaseUser := "SA"

slickCodegenDatabasePassword := ""

slickCodegenDriver := slick.jdbc.H2Profile

slickCodegenJdbcDriver := "org.h2.Driver"

slickCodegenOutputPackage := "org.vincibean.kata.offers.dao"

slickCodegenOutputDir := (sourceManaged in Compile).value

libraryDependencies ++= codegenDependencies

sourceGenerators in Compile += slickCodegen.taskValue
