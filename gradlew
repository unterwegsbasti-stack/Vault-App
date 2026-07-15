#!/usr/bin/env bash

##############################################################################
# Gradle start up script for UN*X
##############################################################################

# Resolve links - $0 may be a link
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/""$link"
  fi
done

APP_NAME=`basename "$PRG"`

# Get standard environment variables
PRGDIR=`dirname "$PRG"`

# Only set APP_HOME if not already set
[ -z "$APP_HOME" ] && APP_HOME=`cd "$PRGDIR/.." ; pwd`

APP_BASE_NAME=`basename "$APP_HOME"`

# Set the JVM maximum heap size if not already set
[ -z "$DEFAULT_JVM_OPTS" ] && DEFAULT_JVM_OPTS='-Xmx1024m'

exec "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"
