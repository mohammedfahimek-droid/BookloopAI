@echo off
setlocal
echo ===================================================
echo     Launching BookLoop AI Console Platform...
echo ===================================================
set PATH=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin;%PATH%
if not exist "bin" mkdir bin
javac -encoding UTF-8 -d bin src\com\bookloop\model\*.java src\com\bookloop\exception\*.java src\com\bookloop\ai\*.java src\com\bookloop\service\*.java src\com\bookloop\repository\*.java src\com\bookloop\ui\*.java src\com\bookloop\Main.java
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b %errorlevel%
)
java -Dfile.encoding=UTF-8 -cp bin com.bookloop.Main %*
endlocal
