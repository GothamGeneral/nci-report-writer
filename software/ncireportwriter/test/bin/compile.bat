@echo off
rem -------------------------------------------------------------------------------------
setlocal enabledelayedexpansion
set ocp=%CLASSPATH%

cd ..
set cp=.
for %%x in (.\lib\*.jar ..\lib\*.jar) do (
  set cp=!cp!;%%x
)
set cp=%cp%;..\build\web\WEB-INF\classes
set CLASSPATH=%cp%

set javac=%JAVA_HOME%\bin\javac

rem -------------------------------------------------------------------------------------
rmdir .\classes /s /q
mkdir .\classes
"%javac%" -d .\classes src/java/gov/nih/nci/evs/reportwriter/test/utils/*.java src/java/gov/nih/nci/evs/reportwriter/test/lexevs/*.java

rem -------------------------------------------------------------------------------------
set CLASSPATH=%ocp%
@echo on
