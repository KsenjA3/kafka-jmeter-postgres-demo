@echo off
echo ========================================
echo Concurrent Kafka-JMeter-PostgreSQL Test
echo ========================================
echo.
echo This script will run 1000 concurrent requests to Kafka
echo and store the results in PostgreSQL database.
echo.

echo Compiling Java application...
javac -cp "postgresql-42.6.0.jar" ConcurrentKafkaDemo.java
if %errorlevel% neq 0 (
    echo Error: Failed to compile Java application
    pause
    exit /b 1
)

echo.
echo Running concurrent Kafka test with 1000 requests...
echo.
java -cp ".;postgresql-42.6.0.jar" ConcurrentKafkaDemo

echo.
echo Test completed!
echo Check the output above for results and performance metrics.
echo.
pause 