#!/bin/sh

if [ "x$1" = "x" ] || [ "x$2" = "x" ] || [ "x$3" = "x" ]; then 
  echo "usage: $0 <pathToSapJCoNativeLib> <versionOfSapJCo> <mavenClassifier>"
  echo " <mavenClassifier> ::= darwinintel64 | ntintel | ntamd64 | linux-x86-64"
  echo " e.g.: $0 lib/libsapjco3.jnilib 3.0.10 darwinintel64"
  exit 
fi

if [ ! -e $1 ]; then
  echo "file $1 does not exist"
  exit
fi

FILE_NAME=$1
FILE_EXT=${FILE_NAME/*./}

mvn install:install-file -Dfile=$1 -DgroupId=com.sap -DartifactId=sap-jco -Dversion=$2 -Dclassifier=$3 -Dpackaging=$FILE_EXT
