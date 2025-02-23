workspace {
    !identifiers hierarchical

    model {
        user = person "Пользователь" "Человек, который хочет посмотреть афишу/купить билет в тг боте"
        admin = person "Администратор" "Контролирует выполнение запросов и управляет системой и пользователями"
        system = softwareSystem "Система" "Система, которая обрабатывает запросы" {
            tg = container "Телеграм бот"
            api = container "API сторонних сервисов" "Получение информации о событиях и обновления"
            bd = container "База данных" {
                tags "Database"
            }
            server = container "Сервер" ""
        }
        #
        #C1
        #
        user -> system "Регистрация"
        system -> admin "Сохранение информации о пользователе"
        admin -> system " Подтверждение сохранения данных"
        system -> user " Подтверждение регистрации"

        user -> system "Вход в систему"
        system -> admin "Проверка введенных данных"
        admin -> system "Подтверждение подлинности данных"
        system -> user "Вход"
        #
        #C2
        #
        user -> system.tg "Отправка данных для регистрации"
        system.tg -> admin "Сохранение информации о пользователe"
        admin -> system.tg "Подтверждение сохранения данных"
        system.tg -> system.bd "Запись нового пользователя"
        system.tg -> user " Подтверждение регистрации"

        system.server -> system.api "Обновление событий"
        user -> system.tg "Выбор события"
        system.tg -> system.server "Запрос данных о событиях"
        system.server -> system.api "Получение информации о событиях"
        system.api -> system.tg "Передача информации о событиях"
        system.tg -> user "Отправка информации о событиях"

        user -> system.tg "Регистрация на событие"
        system.tg -> system.server "Запрос на регистрацию"
        system.server -> system.bd "Запись регистрации пользователя"
        system.bd -> system.server "Подтверждение регистрации"
        system.server -> system.tg "Передача статуса регистрации"
        system.tg -> user "Уведомление о регистрации на событие"



    }

    views {
        systemContext system "Diagram1" {
            include *
            autolayout lr
        }

        container system "Diagram2" {
            include *
            autolayout lr

        }

        styles {
            element "Print" {
                color #000000
            }
            element "Person" {
                background #ffffff
                shape person
            }
            element "Software System" {
                shape cylinder
                background #ffffff
            }
            element "Container" {
                background #ffffff
            }
            element "Database" {
                shape cylinder
            }
        }
    }

    configuration {
        scope softwaresystem
    }
}
