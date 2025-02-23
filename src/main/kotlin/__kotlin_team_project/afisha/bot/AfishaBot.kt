package __kotlin_team_project.afisha.bot

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import jakarta.annotation.PostConstruct
import __kotlin_team_project.afisha.repository.EventRepository
import org.slf4j.LoggerFactory

@Component
class AfishaBot(
    @Value("\${telegram.bot.token}") private val token: String,
    private val eventRepository: EventRepository
) {
    private val logger = LoggerFactory.getLogger(AfishaBot::class.java)
    
    private val bot = bot {
        token = this@AfishaBot.token
        dispatch {
            command("start") {
                logger.info("Received /start command from chat ${message.chat.id}")
                val chatId = ChatId.fromId(message.chat.id)
                bot.sendMessage(
                    chatId = chatId,
                    text = """
                        Привет! Я бот афиши. Я помогу тебе найти интересные мероприятия.
                        
                        Доступные команды:
                        /events - показать все мероприятия
                        /search <текст> - поиск мероприятий по названию
                    """.trimIndent()
                )
            }
            
            command("events") {
                val chatId = ChatId.fromId(message.chat.id)
                val events = eventRepository.findAll()
                if (events.isEmpty()) {
                    bot.sendMessage(
                        chatId = chatId,
                        text = "Пока нет доступных мероприятий"
                    )
                } else {
                    val message = events.joinToString("\n\n") { event ->
                        """
                        🎫 ${event.name}
                        📍 ${event.location}
                        💰 ${event.price} руб.
                        📅 ${event.dateTime.toString()}
                        """.trimIndent()
                    }
                    bot.sendMessage(
                        chatId = chatId,
                        text = message
                    )
                }
            }
            
            command("search") {
                val chatId = ChatId.fromId(message.chat.id)
                val query = args.joinToString(" ")
                if (query.isBlank()) {
                    bot.sendMessage(
                        chatId = chatId,
                        text = "Пожалуйста, укажите текст для поиска после команды /search"
                    )
                    return@command
                }
                
                val events = eventRepository.findByNameContainingIgnoreCase(query)
                if (events.isEmpty()) {
                    bot.sendMessage(
                        chatId = chatId,
                        text = "Мероприятий по запросу '$query' не найдено"
                    )
                } else {
                    val message = events.joinToString("\n\n") { event ->
                        """
                        🎫 ${event.name}
                        📍 ${event.location}
                        💰 ${event.price} руб.
                        📅 ${event.dateTime.toString()}
                        """.trimIndent()
                    }
                    bot.sendMessage(
                        chatId = chatId,
                        text = message
                    )
                }
            }
        }
    }

    @PostConstruct
    fun start() {
        logger.info("Starting Telegram bot")
        bot.startPolling()
    }
} 