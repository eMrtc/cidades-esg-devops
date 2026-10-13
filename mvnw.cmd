@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM
@REM Optional ENV vars:
@REM   JAVA_HOME - location of a JDK home dir, required when type is not auto
@REM   MAVEN_BATCH_ECHO - set to "on" to enable the echoing of the batch commands
@REM   MAVEN_BATCH_PAUSE - set to "on" to wait for a keystroke before ending
@REM   MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_SAVE_ERRORLEVEL__=
@SET __MVNW_SAVE_JAVA_HOME__=

@setlocal

@SET MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
@IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir

@SET EXEC_DIR=%CD%
@SET WDIR=%EXEC_DIR%
:findBaseDir
@IF EXIST "%WDIR%"\.mvn goto baseDirFound
@cd ..
@SET WDIR=%CD%
@IF NOT "%WDIR%"=="%EXEC_DIR%" goto findBaseDir
@SET WDIR=%EXEC_DIR%
:baseDirFound
@SET MAVEN_PROJECTBASEDIR=%WDIR%
@cd "%EXEC_DIR%"

:endDetectBaseDir

@IF NOT EXIST "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" (
@IF NOT "%MVNW_REPOURL%" == "" (
@SET MAVEN_WRAPPER_JAR_URL=%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar
) ELSE (
@SET MAVEN_WRAPPER_JAR_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar
)

echo Downloading: %MAVEN_WRAPPER_JAR_URL%

powershell -Command "&{"^
"$webclient = new-object System.Net.WebClient;"^
"if (-not ([string]::IsNullOrEmpty('%MVNW_USERNAME%') -and [string]::IsNullOrEmpty('%MVNW_PASSWORD%'))) {"^
"$webclient.Credentials = new-object System.Net.NetworkCredential('%MVNW_USERNAME%', '%MVNW_PASSWORD%');"^
"}"^
"[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $webclient.DownloadFile('%MAVEN_WRAPPER_JAR_URL%', '%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar')"^
"}"
)

@SET MAVEN_JAVA_EXE=%JAVA_HOME%\bin\java.exe
@SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
@SET WRAPPER_CLASSPATH=%WRAPPER_JAR%

@SET MAVEN_CMD_LINE_ARGS=%MAVEN_CONFIG% %*

%MAVEN_JAVA_EXE% ^
  %MAVEN_OPTS% ^
  %MAVEN_DEBUG_OPTS% ^
  -classpath %WRAPPER_CLASSPATH% ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
@IF ERRORLEVEL 1 GOTO error
@GOTO end

:error
@SET ERROR_CODE=1

:end
@endlocal & SET ERROR_CODE=%ERROR_CODE%

@IF NOT "%MAVEN_BATCH_PAUSE%"=="on" GOTO cleanUp

:pause
@echo.
@echo Press any key to continue...
@pause 1>NUL

:cleanUp
@ENDLOCAL & EXIT /B %ERROR_CODE%
