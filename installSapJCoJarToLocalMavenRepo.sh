#!/bin/sh

if [ "x$1" = "x" ] || [ "x$2" = "x" ]; then 
  echo "usage: $0 <pathToSapJCoJar> <versionOfSapJCo>, e.g. $0 lib/sapjco.jar 3.0.10"
  exit 
fi

if [ ! -e $1 ]; then
  echo "file $1 does not exist"
  exit
fi

mvn install:install-file -Dfile=$1 -DgroupId=org.hibersap -DartifactId=hibersap-sapjco3 -Dversion=$2 -Dpackaging=jar


