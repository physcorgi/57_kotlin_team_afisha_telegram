package com.example.blank

import eu.vendeli.tgbot.TelegramBot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@SpringBootApplication
class TgBotApplication {
    
    @Bean
    fun telegramBot(@Value("\${ktgram.bot.token}") token: String): TelegramBot {
        return TelegramBot(token)
    }
}

@Component
class TelegramBotStarter(private val bot: TelegramBot) : ApplicationListener<ApplicationReadyEvent> {
    
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        // Запускаем бота в корутине после полной инициализации приложения
        CoroutineScope(Dispatchers.Default).launch {
            try {
                bot.handleUpdates()
            } catch (e: Exception) {
                println("Ошибка при запуске бота: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<TgBotApplication>(*args)
}
