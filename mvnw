#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, version 3.3.2
#
# Optional ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir, required when type is not auto
#   MAVEN_OPTS - parameters passed to the Java VM when running Maven
#   MAVEN_BATCH_ECHO - set to "on" to enable the echoing of the batch commands
#   MAVEN_BATCH_PAUSE - set to "on" to wait for a keystroke before ending
#   MAVEN_OPTS - extra JVM options for Maven
#   MAVEN_SKIP_RC - set to any non-empty value to skip loading of .mavenrc
# ----------------------------------------------------------------------------

if [ -z "$MAVEN_SKIP_RC" ] ; then
  if [ -f "$HOME/.mavenrc" ] ; then
    . "$HOME/.mavenrc"
  fi
fi

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
mingw=false
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  MINGW*) mingw=true;;
  Darwin*) darwin=true
    # Use /usr/libexec/java_home if available, otherwise fall through
    # see https://developer.apple.com/library/mac/qa/qa1170/_index.html
    if [ -z "$JAVA_HOME" ]; then
      if [ -x "/usr/libexec/java_home" ]; then
        JAVA_HOME="`/usr/libexec/java_home`"; export JAVA_HOME
      fi
    fi
    ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] &&
    CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# For Mingw, ensure paths are in UNIX format before anything is touched
if $mingw ; then
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME="`(cd "$JAVA_HOME"; pwd)`"
fi

if [ -z "$JAVA_HOME" ]; then
  javaExecutable="`which javac`"
  if [ -n "$javaExecutable" ] && ! [ "`expr \"$javaExecutable\" : '\([^ ]*\)'`" = "javac" ]; then
    # readlink(1) is not available as standard on Solaris 10.
    readLink=`which readlink`
    if [ ! `expr "$readLink" : '\([^ ]*\)'` = "readlink" ]; then
      if $darwin ; then
        javaHome="`dirname \"$javaExecutable\"`"
        javaExecutable="`cd \"$javaHome\" && pwd -P`/javac"
      else
        javaExecutable="`readlink -f \"$javaExecutable\"`"
      fi
    else
      javaExecutable="`readlink -f \"$javaExecutable\"`"
    fi
    javaHome="`dirname \"$javaExecutable\"`"
    javaHome=`expr "$javaHome" : '\(.*\)/bin'`
    JAVA_HOME="$javaHome"
    export JAVA_HOME
  fi
fi

if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD="`\\unset -f command; \\command -v java`"
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." >&2
  echo "  We cannot execute $JAVACMD" >&2
  exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
  echo "Warning: JAVA_HOME environment variable is not set."
fi

# traverses directory structure from process work directory to filesystem root,
# searching for .mvn/wrapper/maven-wrapper.properties
find_maven_basedir() {
  if [ -z "$1" ]
  then
    echo "Path not specified to find_maven_basedir"
    return 1
  fi

  basedir="$1"
  wdir="$1"
  while [ "$wdir" != '/' ] ; do
    if [ -d "$wdir"/.mvn ] ; then
      basedir=$wdir
      break
    fi
    # workaround for JBEAP-8937 (on Solaris 10/Sparc)
    if [ -d "${wdir}" ]; then
      wdir=`cd "$wdir/.."; pwd`
    fi
    # end of workaround
  done
  printf '%s' "$(cd "$basedir"; pwd -P)"
}

# concatenates all lines of a file
concat_lines() {
  if [ -f "$1" ]; then
    echo "$(tr -s '\n' ' ' < "$1")"
  fi
}

log() {
  if [ "$MVNW_VERBOSE" = true ]; then
    printf '%s\n' "$1"
  fi
}

BASE_DIR=$(find_maven_basedir "$(dirname $0)")
if [ -z "$BASE_DIR" ]; then
  exit 1;
fi

MAVEN_PROJECTBASEDIR=${MAVEN_BASEDIR:-"$BASE_DIR"}
export MAVEN_PROJECTBASEDIR
log "$MAVEN_PROJECTBASEDIR"

##########################################################################################
# Extension to allow automatically downloading the maven-wrapper.jar from Maven central
# This allows using the maven wrapper in certain environments without a .m2 directory
##########################################################################################
# If specified, validate the SHA-256 sum of the maven-wrapper.jar file
wrapperSha256Sum=""
while IFS="=" read key value; do
  case "$key" in (wrapperSha256Sum) wrapperSha256Sum=$value; break ;;
  esac
done < "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

if [ -r "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar" ]; then
  log "Found .mvn/wrapper/maven-wrapper.jar"
else
  log "Couldn't find .mvn/wrapper/maven-wrapper.jar, downloading it ..."
  if [ -n "$MVNW_REPOURL" ]; then
    wrapperUrl="$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
  else
    wrapperUrl="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
  fi
  while IFS="=" read key value; do
    case "$key" in (distributionUrl) wrapperUrl="$value"; break ;;
    esac
  done < "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"
  log "Downloading from: $wrapperUrl"
  wrapperJarPath="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
  if $darwin; then
    if command -v wget > /dev/null; then
      log "Found wget ... using wget"
      [ "$MVNW_VERBOSE" = true ] && QUIET="" || QUIET="--quiet"
      if [ -z "$MVNW_USERNAME" ] || [ -z "$MVNW_PASSWORD" ]; then
        wget $QUIET "$wrapperUrl" -O "$wrapperJarPath" || rm -f "$wrapperJarPath"
      else
        wget $QUIET --http-user="$MVNW_USERNAME" --http-password="$MVNW_PASSWORD" "$wrapperUrl" -O "$wrapperJarPath" || rm -f "$wrapperJarPath"
      fi
    elif command -v curl > /dev/null; then
      log "Found curl ... using curl"
      [ "$MVNW_VERBOSE" = true ] && QUIET="" || QUIET="--silent"
      if [ -z "$MVNW_USERNAME" ] || [ -z "$MVNW_PASSWORD" ]; then
        curl $QUIET -o "$wrapperJarPath" "$wrapperUrl" -f || rm -f "$wrapperJarPath"
      else
        curl $QUIET --user "$MVNW_USERNAME:$MVNW_PASSWORD" -o "$wrapperJarPath" "$wrapperUrl" -f || rm -f "$wrapperJarPath"
      fi
    else
      log "Falling back to Java to download"
    fi
  else
    if command -v wget > /dev/null; then
      log "Found wget ... using wget"
      [ "$MVNW_VERBOSE" = true ] && QUIET="" || QUIET="--quiet"
      if [ -z "$MVNW_USERNAME" ] || [ -z "$MVNW_PASSWORD" ]; then
        wget $QUIET "$wrapperUrl" -O "$wrapperJarPath" || rm -f "$wrapperJarPath"
      else
        wget $QUIET --http-user="$MVNW_USERNAME" --http-password="$MVNW_PASSWORD" "$wrapperUrl" -O "$wrapperJarPath" || rm -f "$wrapperJarPath"
      fi
    elif command -v curl > /dev/null; then
      log "Found curl ... using curl"
      [ "$MVNW_VERBOSE" = true ] && QUIET="" || QUIET="--silent"
      if [ -z "$MVNW_USERNAME" ] || [ -z "$MVNW_PASSWORD" ]; then
        curl $QUIET -o "$wrapperJarPath" "$wrapperUrl" -f || rm -f "$wrapperJarPath"
      else
        curl $QUIET --user "$MVNW_USERNAME:$MVNW_PASSWORD" -o "$wrapperJarPath" "$wrapperUrl" -f || rm -f "$wrapperJarPath"
      fi
    else
      log "Falling back to Java to download"
    fi
  fi
fi
##########################################################################################
# End of extension
##########################################################################################

export MAVEN_OPTS
# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] &&
    CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  [ -n "$MAVEN_PROJECTBASEDIR" ] &&
    MAVEN_PROJECTBASEDIR=`cygpath --path --windows "$MAVEN_PROJECTBASEDIR"`
fi

# Provide a "standardized" way to retrieve the CLI args that will
# be supplied to the JVM.  This is a necessary step to ensure coverage
# of concepts like stopword parsing, parameter substitution, etc.
# @see https://github.com/openjdk/jdk/blob/master/src/java.base/share/classes/java/lang/ProcessBuilder.java#L1661
MAVEN_CMD_LINE_ARGS="$MAVEN_CONFIG $@"
export MAVEN_CMD_LINE_ARGS

WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

WRAPPER_CLASSPATH="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"

exec "$JAVACMD" \
  $MAVEN_OPTS \
  $MAVEN_DEBUG_OPTS \
  -classpath "$WRAPPER_CLASSPATH" \
  "-Dmaven.multiModuleProjectDirectory=${MAVEN_PROJECTBASEDIR}" \
  ${WRAPPER_LAUNCHER} "$MAVEN_CONFIG" "$@"
