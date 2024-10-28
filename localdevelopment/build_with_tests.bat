@ECHO OFF
SETLOCAL enableextensions enabledelayedexpansion

SET PATH=C:\Projects\POSSIBLE\tools\jdk17\bin

ECHO.
ECHO build WITH tests
ECHO.


CD..

call gradlew.bat backend:cleanBuild
call gradlew.bat backend:clean test --info

ECHO.
ECHO DONE
ECHO.
ECHO.
PAUSE
ECHO.

