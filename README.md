
# Приложение Who Wins
Веб сервис для составления прогнозов на киберспортивные матчи по CS:GO

 ---
## Используемый стек:
 - Java 17 (должен работать и на более старых версиях)
 - Spring Boot 3.1.0
 - Spring Kafka 3.0.4
 - Lombok 1.18.26
 - JSoup 1.15.4 (для микросервиса WHO_WINS_PARSER)
 - Selenium 4.6.0 (для микросервиса WHO_WINS_PARSER)
 - Google Chrome 1.0.7 
 - Selenium Chrome Driver 1.0.7
 - MySQL (для микросервиса WHO_WINS_DATABASE)

---
## Настройка и запуск

### WHO_WINS_APPLICATION

Пример application.yaml файла:

    server:
        port: <service_app_port>

    spring:
        application:
            microservice:
                parser:
                    name: WHO_WINS_PARSER
                    url: http://<service_parser_url>/parse
                database:
                    name: WHO_WINS_DATABASE
                    url: http://<service_database_url>

    kafka:
        server: http://<kafka_url>:<kafka_port>
        producer:
            topic: from_main_to_parser
            group-id: ${kafka.producer.topic}

### WHO_WINS_PARSER

Пример application.yaml файла:

    server:
        port: <service_parser_port>
        
    kafka:
        server: http://<kafka_url>:<kafka_port>
        producer:
            topic: from_parser_to_database
            group-id: ${kafka.producer.topic}
        consumer:
            topic: from_main_to_parser
            group-id: ${kafka.consumer.topic}


### WHO_WINS_DATABASE
Пример application.yaml файла:

    server:
        port: <service_database_port>
    
    spring:
    datasource:
    url: jdbc:mysql://<database_path>/<database_name>
    username: <database_username>
    password: <database_password>
    driver-class-name: com.mysql.cj.jdbc.Driver
    
    jpa:
        show-sql: false
        hibernate:
            ddl-auto: update
    
    kafka:
        server: http://<kafka_url>:<kafka_port>
        consumer:
            topic: from_parser_to_database
            group-id: "${kafka.consumer.topic}"

---
## API микросервисов

### WHO_WINS_APPLICATION

_GET /parse/results/{count}_

_GET /parse/results/{from}/{to}_

_GET /parse/results/today_

_GET /parse/results/yesterday_


# Файл README.md не закончен, позже будет собрана полная версия