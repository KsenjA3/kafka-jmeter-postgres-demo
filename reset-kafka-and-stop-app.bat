@echo off
REM Настройки
set KAFKA_CONTAINER=kafka        REM Имя контейнера Kafka
set KAFKA_TOPIC=benchmark_topic  REM Имя топика, который нужно очистить
set KAFKA_BOOTSTRAP=localhost:9092  REM Адрес Kafka bootstrap server

REM 1. Удалить топик Kafka
echo Удаляю топик %KAFKA_TOPIC%...
docker exec %KAFKA_CONTAINER% kafka-topics --delete --topic %KAFKA_TOPIC% --bootstrap-server %KAFKA_BOOTSTRAP%

REM 2. Пересоздать топик Kafka (если нужно)
echo Создаю топик %KAFKA_TOPIC% заново...
docker exec %KAFKA_CONTAINER% kafka-topics --create --if-not-exists --topic %KAFKA_TOPIC% --bootstrap-server %KAFKA_BOOTSTRAP% --partitions 24 --replication-factor 1

echo Готово.

echo.
echo Running JDBC monitoring scenario in parallel...
start "JDBC Monitor" cmd /c "cd /d c:\Users\User\apache-jmeter-5.6.3 && bin\jmeter.bat -n -t %DB_TEST_PLAN% -l %DB_RESULTS_FILE% -e -o %DB_HTML_REPORT%"
