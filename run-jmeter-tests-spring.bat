@echo off
echo Running JMeter tests against Spring Boot application...
echo.

echo Starting Kafka and PostgreSQL containers...
docker-compose up -d

echo.
echo Waiting for services to be ready...
timeout /t 30 /nobreak

echo.
echo Running JMeter tests...

REM Run 1000 concurrent users test
echo Running 1000 concurrent users test...
cd C:\apache-jmeter
bin\jmeter.bat -n -t "C:\testProject3\jmeter\1000-concurrent-test-plan.jmx" -l "C:\testProject3\jmeter\1000-concurrent-test-results.jtl" -e -o "C:\testProject3\jmeter\1000-concurrent-html-report"

echo.
echo Running 5000 concurrent users test...
bin\jmeter.bat -n -t "C:\testProject3\jmeter\5000-concurrent-test-plan.jmx" -l "C:\testProject3\jmeter\5000-concurrent-test-results.jtl" -e -o "C:\testProject3\jmeter\5000-concurrent-html-report"

echo.
echo Running database query test...
bin\jmeter.bat -n -t "C:\testProject3\jmeter\database-query-test-plan.jmx" -l "C:\testProject3\jmeter\database-query-results.jtl" -e -o "C:\testProject3\jmeter\database-query-html-report"

echo.
echo Tests completed!
pause 