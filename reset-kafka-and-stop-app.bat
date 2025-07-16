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
start "JDBC Monitor" cmd /c "cd /d c:\Users\User\apache-jmeter-5.6.3 && bin\jmeter.bat -n -t %DB_TEST_PLAN% -l %DB_RESULTS_FILE% -e -o %DB_HTML_REPORT% -j %JMETER_JDBC_LOG%"



                       ${__groovy(
                        def currentCountStr = vars.get('currentCount') ?: '0';
                        def currentCount = Integer.parseInt(currentCountStr);

                        def expectedCount = Integer.parseInt(vars.get('expectedCount') ?: '0');
                        def startWaitTime = Long.parseLong(vars.get('startWaitTime') ?: '0');
                        def currentTime = System.currentTimeMillis();
                        def timeout = 600000; // 10 минут в миллисекундах
                        log.info("Сurrent Count: " + currentCount)
                        log.info("Expected Count: " + expectedCount)
                        (currentCount &lt; expectedCount) || ((currentTime - startWaitTime) &lt; timeout);
                      )}

                        ${__groovy(
                              def raw = vars.get('currentCount') ?: '[{count=0}]';
                              def currentCount = raw.replaceAll(/[\[\]\{\}a-z=]/, '').trim().toInteger();

                              def expectedCount = Integer.parseInt(vars.get('expectedCount') ?: '0');
                              def startWaitTime = Long.parseLong(vars.get('startWaitTime') ?: '0');
                              def currentTime = System.currentTimeMillis();
                              def timeout = 600000;

                              log.info("Current Count: " + currentCount);
                              log.info("Expected Count: " + expectedCount);
                              log.info("Elapsed: " + (currentTime - startWaitTime) + " ms");

                              (currentCount &lt; expectedCount) || ((currentTime - startWaitTime) &lt; timeout);
                          )}

                                    ${__groovy(
                                          (vars.get('currentCount') ?: '[{count=0}]')
                                          .replaceAll('\\[\\{count=','')
                                          .replaceAll('\\}','')
                                          .trim()
                                          .toInteger() &lt; (vars.get('expectedCount') ?: '0').toInteger()
                                            ||
                                          (System.currentTimeMillis() - (vars.get('startWaitTime') ?: '0').toLong()) &lt; 600000
                                        )}
