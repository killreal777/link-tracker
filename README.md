![Build](https://github.com/central-university-dev/backend-academy-2025-spring-template/actions/workflows/build.yaml/badge.svg)

# Link Tracker

### Запуск

1. Обеспечить передачу токенов в конфигурацию приложений: 
[bot](bot/src/main/resources/application.yaml),
[scrapper](scrapper/src/main/resources/application.yaml)
2. Запустить приложения:
[bot](bot/src/main/java/backend/academy/bot/BotApplication.java),
[scrapper](scrapper/src/main/java/backend/academy/scrapper/ScrapperApplication.java)

Проект сделан в рамках курса Академия Бэкенда.

Приложение для отслеживания обновлений контента по ссылкам.
При появлении новых событий отправляется уведомление в Telegram.

Проект написан на `Java 23` с использованием `Spring Boot 3`.

Проект состоит из 2-х приложений:
* Bot
* Scrapper

Для работы требуется БД `PostgreSQL`. Присутствует опциональная зависимость на `Kafka`.

Для дополнительной справки: [HELP.md](./HELP.md)
