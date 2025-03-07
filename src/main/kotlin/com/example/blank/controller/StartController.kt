package com.example.blank.controller

import eu.vendeli.tgbot.TelegramBot
import eu.vendeli.tgbot.annotations.CommandHandler
import eu.vendeli.tgbot.api.message.message
import eu.vendeli.tgbot.types.User
import eu.vendeli.tgbot.types.internal.ProcessedUpdate
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets

fun loadRules(): String {
    try {
        val resource = ClassPathResource("texts/rules.txt")
        return resource.inputStream.readAllBytes().toString(StandardCharsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
        return "Не удалось загрузить правила. Пожалуйста, попробуйте позже."
    }
}

@Component
class StartController {
    @CommandHandler(["/start"])
    suspend fun start(update: ProcessedUpdate, bot: TelegramBot, user: User) {
        message{ "Test" }.inlineKeyboardMarkup {
            callbackData("Правила"){"rules"}
            callbackData("Настройки"){"settings"}
            callbackData("О проекте"){"help"}
            callbackData("Профиль"){"about"}
        }.send(user, bot)
    }
    @CommandHandler.CallbackQuery(["rules"])
    suspend fun rules(update: ProcessedUpdate, bot: TelegramBot, user: User) {
        message{
            loadRules()
        }.send(user, bot)
    }
}