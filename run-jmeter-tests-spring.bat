@echo off

REM Метка времени для уникальных папок (формат: YYYY-MM-DD_HH-MM-SS)
set "TS=%DATE:~6,4%-%DATE:~3,2%-%DATE:~0,2%_%TIME:~0,2%-%TIME:~3,2%-%TIME:~6,2%"
set "TS=%TS: =0%"

REM Получить абсолютный путь к корню проекта (где лежит этот батник)
set "PROJECT_DIR=%~dp0"
if "%PROJECT_DIR:~-1%"=="\" set "PROJECT_DIR=%PROJECT_DIR:~0,-1%"

REM Пути к тест-плану и папкам для отчётов
set "JMETER_DIR=%PROJECT_DIR%\jmeter"
set "TEST_PLAN=%JMETER_DIR%\50-concurrent-messages-simple.jmx"
set "RESULTS_FILE=%JMETER_DIR%\50-concurrent-messages-simple-results-%TS%.jtl"
set "HTML_REPORT=%JMETER_DIR%\50-concurrent-messages-simple-html-report-%TS%"
set "DB_RESULTS_FILE=%JMETER_DIR%\database-query-results-%TS%.jtl"
set "DB_HTML_REPORT=%JMETER_DIR%\database-query-html-report-%TS%"
set "DB_TEST_PLAN=%JMETER_DIR%\JDBC-Request1.jmx"
set "JMETER_JDBC_LOG=%JMETER_DIR%\jmeter-jdbc-%TS%.log"
set "JMETER_HTTP_LOG=%JMETER_DIR%\jmeter-http-%TS%.log"

REM Удалить старые папки отчетов (если есть)
for /d %%D in ("%JMETER_DIR%\50-concurrent-messages-simple-html-report*") do rd /s /q "%%D"
for /d %%D in ("%JMETER_DIR%\database-query-html-report*") do rd /s /q "%%D"

if not exist "%HTML_REPORT%" mkdir "%HTML_REPORT%"
if not exist "%DB_HTML_REPORT%" mkdir "%DB_HTML_REPORT%"



echo.
echo Running JDBC monitoring scenario in parallel...
start "JDBC Monitor" cmd /c "cd /d c:\Users\User\apache-jmeter-5.6.3 && bin\jmeter.bat -n -t %DB_TEST_PLAN% -l %DB_RESULTS_FILE% -e -o %DB_HTML_REPORT% -j %JMETER_JDBC_LOG%"



echo ========================================
echo JMeter End-to-End Kafka Test (Simple Scenario)
echo ========================================
echo.

echo.
echo Running JMeter test with end-to-end timing...
echo Test Plan: %TEST_PLAN%"
echo Results: %RESULTS_FILE%"
echo HTML Report: %HTML_REPORT%"
echo.
cd /d c:\Users\User\apache-jmeter-5.6.3
bin\jmeter.bat -n -t %TEST_PLAN% -l %RESULTS_FILE% -e -o %HTML_REPORT% -j %JMETER_HTTP_LOG%"

cd /d "%PROJECT_DIR%"
echo.
echo Test completed!
echo Check the HTML reports for detailed results:
echo   %HTML_REPORT%\index.html
echo   %DB_HTML_REPORT%\index.html
pause 

