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

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "ERROR: Missing required arguments"
  echo "Usage: $0 <pathToSapJCoJar> <versionOfSapJCo>"
  echo "Example: $0 lib/sapjco.jar 3.1.13"
  echo ""
  echo "This script installs the SAP JCo JAR file to your local Maven repository."
  exit 1
fi

if [ ! -e "$1" ]; then
  echo "ERROR: file $1 does not exist"
  exit 1
fi

mvn install:install-file -Dfile="$1" -DgroupId=org.hibersap -DartifactId=com.sap.conn.jco.sapjco3 -Dversion="$2" -Dpackaging=jar


