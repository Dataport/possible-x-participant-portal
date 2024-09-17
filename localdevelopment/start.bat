@ECHO OFF
SETLOCAL enableextensions enabledelayedexpansion

SET PATH=C:\Projects\POSSIBLE\tools\jdk17\bin

SET fh.catalog.url=http://localhost:9090
SET edc.mgmt-base-url=http://localhost:9090

ECHO.
ECHO build and run
ECHO.


CD..

call gradlew.bat backend:cleanBuild
call gradlew.bat backend:bootRun

ECHO.
ECHO DONE
ECHO.
ECHO.
PAUSE
ECHO.

