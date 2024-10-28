@ECHO OFF
SETLOCAL enableextensions enabledelayedexpansion

SET PATH=C:\Projects\POSSIBLE\tools\jdk17\bin

ECHO.
ECHO build WITHOUT tests
ECHO.


CD..

call gradlew.bat backend:cleanBuild

ECHO.
ECHO DONE
ECHO.
ECHO.
PAUSE
ECHO.

