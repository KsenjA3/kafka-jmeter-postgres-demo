@echo off
echo Starting Spring Boot Kafka-JMeter-PostgreSQL Application...
echo.

echo Building the application...
call mvn clean package -DskipTests

echo.
echo Starting the application...
java -jar target\kafka-jmeter-postgres-demo-1.0-SNAPSHOT.jar

pause 