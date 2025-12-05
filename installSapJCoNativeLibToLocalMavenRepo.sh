#!/bin/sh

#
# Copyright (c) 2008-2025 tech@spree GmbH
#
# This file is part of Hibersap.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this software except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

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

mvn install:install-file -Dfile=$1 -DgroupId=org.hibersap -DartifactId=com.sap.conn.jco.sapjco3 -Dversion=$2 -Dclassifier=$3 -Dpackaging=$FILE_EXT
