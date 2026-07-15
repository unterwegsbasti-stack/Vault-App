@echo off
SETLOCAL

REM -----------------------------------------------------------------------------
REM Gradle start up script for Windows
REM -----------------------------------------------------------------------------

set DIRNAME=%~dp0
set APP_HOME=%DIRNAME%..\

%APP_HOME%\gradle\wrapper\gradle-wrapper.jar %*
