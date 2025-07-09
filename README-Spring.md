# Spring Boot Kafka-JMeter-PostgreSQL Integration

Это Spring Boot версия приложения для интеграции Apache Kafka, Apache JMeter и PostgreSQL с использованием контейнеров Docker.

## Архитектура

- **Spring Boot 3.2.0** - основной фреймворк
- **Spring Data JPA** - работа с PostgreSQL
- **Spring for Apache Kafka** - интеграция с Kafka
- **Docker Compose** - Kafka и PostgreSQL в контейнерах

## Быстрый старт

### 1. Запуск инфраструктуры

```bash
# Запуск Kafka и PostgreSQL в контейнерах
docker-compose up -d
```

### 2. Запуск Spring Boot приложения

```bash
# Сборка и запуск
start-spring-app.bat
```

Или вручную:
```bash
mvn clean package
java -jar target/kafka-jmeter-postgres-demo-1.0-SNAPSHOT.jar
```

### 3. Запуск JMeter тестов

```bash
run-jmeter-tests-spring.bat
```

## API Endpoints

### Основные эндпоинты

- `POST /api/messages` - отправить сообщение в Kafka
- `GET /api/messages` - получить все сообщения из БД
- `GET /api/count` - получить количество сообщений
- `GET /api/statistics` - получить статистику
- `GET /api/health` - проверка состояния
- `DELETE /api/messages` - очистить все сообщения

### Совместимость с JMeter (legacy endpoints)

- `POST /send` - отправить сообщение (совместимость с JMeter)
- `GET /query` - запрос к базе данных (совместимость с JMeter)

## Конфигурация

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/benchmark
    username: benchmark
    password: benchmark
  
  kafka:
    bootstrap-servers: localhost:9094
    consumer:
      group-id: spring-demo-group
    producer:
      acks: all
      retries: 3

kafka:
  topic: benchmark_topic

server:
  port: 8080
```

### Docker Compose

- **Kafka**: localhost:9094
- **PostgreSQL**: localhost:5432
- **Zookeeper**: localhost:2181

## Структура проекта

```
src/main/java/com/test/
├── KafkaJmeterPostgresApplication.java    # Главный класс
├── config/
│   └── KafkaConfig.java                  # Конфигурация Kafka
├── controller/
│   └── MessageController.java            # REST API контроллер
├── kafka/
│   ├── MessageProducer.java              # Kafka Producer
│   └── MessageConsumer.java              # Kafka Consumer
├── model/
│   └── Message.java                      # JPA сущность
├── repository/
│   └── MessageRepository.java            # Spring Data репозиторий
└── service/
    └── MessageService.java               # Бизнес-логика
```

## Тестирование

### 1. Проверка здоровья приложения

```bash
curl http://localhost:8080/api/health
```

### 2. Отправка сообщения

```bash
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{"content": "Test message from curl"}'
```

### 3. Получение сообщений

```bash
curl http://localhost:8080/api/messages
```

### 4. Получение статистики

```bash
curl http://localhost:8080/api/statistics
```

## JMeter тест-планы

Приложение совместимо с существующими JMeter тест-планами:

- `1000-concurrent-test-plan.jmx` - 1000 одновременных пользователей
- `5000-concurrent-test-plan.jmx` - 5000 одновременных пользователей
- `database-query-test-plan.jmx` - тестирование запросов к БД

## Мониторинг

### Логи

```bash
# Просмотр логов приложения
tail -f logs/spring-boot.log
```

### Kafka

```bash
# Просмотр топиков
docker exec kafka kafka-topics --list --bootstrap-server localhost:9092

# Просмотр сообщений в топике
docker exec kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic benchmark_topic --from-beginning
```

### PostgreSQL

```bash
# Подключение к базе данных
docker exec -it postgres psql -U benchmark -d benchmark

# Просмотр сообщений
SELECT * FROM messages ORDER BY created_at DESC LIMIT 10;
```

## Производительность

### Настройки Kafka

- **Partitions**: 24 (для высокой пропускной способности)
- **Replication Factor**: 1 (для разработки)
- **Batch Size**: 16384 bytes
- **Linger MS**: 1ms

### Настройки PostgreSQL

- **Connection Pool**: HikariCP (по умолчанию)
- **Indexes**: на created_at, source, sequence_number
- **Batch Processing**: включено

## Troubleshooting

### Проблемы с подключением к Kafka

1. Проверьте, что контейнеры запущены:
```bash
docker-compose ps
```

2. Проверьте логи Kafka:
```bash
docker logs kafka
```

### Проблемы с подключением к PostgreSQL

1. Проверьте подключение:
```bash
docker exec -it postgres psql -U benchmark -d benchmark -c "SELECT 1;"
```

2. Проверьте логи PostgreSQL:
```bash
docker logs postgres
```

### Проблемы с JMeter

1. Убедитесь, что приложение запущено на порту 8080
2. Проверьте health endpoint: `http://localhost:8080/api/health`
3. Проверьте legacy endpoints: `http://localhost:8080/send`, `http://localhost:8080/query`

## Разработка

### Добавление новых эндпоинтов

1. Добавьте метод в `MessageController`
2. Добавьте бизнес-логику в `MessageService`
3. При необходимости добавьте методы в `MessageRepository`

### Изменение конфигурации Kafka

1. Отредактируйте `application.yml`
2. При необходимости измените `KafkaConfig.java`

### Добавление новых полей в Message

1. Добавьте поле в `Message.java`
2. Обновите базу данных (Hibernate сделает это автоматически)
3. Обновите репозиторий и сервис при необходимости 