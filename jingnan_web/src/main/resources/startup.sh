#!/bin/sh
# Copyright 1999-2022 Kaikeba NB Group Holding Ltd.
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

export JAVA_HOME
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib



export SERVER_NAME="jingnan_web"
export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=`cd $(dirname $0)/.; pwd`
export DEFAULT_SEARCH_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"
export CUSTOM_SEARCH_LOCATIONS=${DEFAULT_SEARCH_LOCATIONS},file:${BASE_DIR}/conf/

#JAVA_OPT="${JAVA_OPT} -server -Xms512m -Xmx512m -Xmn256 -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m"
#JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BASE_DIR}/logs/java_heapdump.hprof"
#JAVA_OPT="${JAVA_OPT} -XX:-UseLargePages"
JAVA_OPT="${JAVA_OPT} -jar ${BASE_DIR}/${SERVER_NAME}*.jar"
JAVA_OPT="${JAVA_OPT} ${JAVA_OPT_EXT}"

JAVA_OPT="${JAVA_OPT} --spring.config.location=${CUSTOM_SEARCH_LOCATIONS}"
if [ ! -d "${BASE_DIR}/logs" ]; then
  mkdir ${BASE_DIR}/logs
fi
echo "$JAVA ${JAVA_OPT}"

if [ ! -f "${BASE_DIR}/logs/${SERVER_NAME}.out" ]; then
  touch "${BASE_DIR}/logs/${SERVER_NAME}.out"
fi

echo "$JAVA ${JAVA_OPT}" > ${BASE_DIR}/logs/${SERVER_NAME}.out 2>&1 &
nohup $JAVA ${JAVA_OPT} jingnan_web.jingnan_web >> ${BASE_DIR}/logs/${SERVER_NAME}.out 2>&1 &
echo "server is starting，you can check the ${BASE_DIR}/logs/${SERVER_NAME}.out"
