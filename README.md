# siale

[![Dependency Status](https://www.versioneye.com/user/projects/592f3f5bbafc55002b9fda67/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/592f3f5bbafc55002b9fda67) [![Build Status](https://travis-ci.org/DSI-Ville-Noumea/siale.svg?branch=master)](https://travis-ci.org/DSI-Ville-Noumea/siale)

#### Installation

Ajout des dépendances dans le repo maven local.

```
mvn install:install-file -DgroupId=com.bo -DartifactId=cecore -Dversion=11.6 -Dfile=./lib/cecore.jar -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=com.bo -DartifactId=cesession -Dversion=11.6 -Dfile=./lib/cesession.jar -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=com.bo -DartifactId=celib -Dversion=11.6 -Dfile=./lib/celib.jar -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=com.bo -DartifactId=logging -Dversion=11.6 -Dfile=./lib/logging.jar -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=com.bo -DartifactId=ebus405 -Dversion=11.5.8.834 -Dfile=./lib/ebus405.jar -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=com.bo -DartifactId=corbaidl -Dversion=12.5.0.1190 -Dfile=./lib/corbaidl.jar -Dpackaging=jar -DgeneratePom=true
mvn install:install-file -DgroupId=color.box -DartifactId=colorbox -Dversion=1.0 -Dfile=./lib/colorbox.jar -Dpackaging=jar -DgeneratePom=true
```



