environnement set MAVEN_OPTS=-Duser.timezone="Pacific/Noumea"
mvn install:install-file -Dfile=lib/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=10.2.0.3 -Dpackaging=jar -DgeneratePom=true
