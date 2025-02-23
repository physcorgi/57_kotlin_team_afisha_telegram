package __kotlin_team_project.afisha.service

import __kotlin_team_project.afisha.repository.EventRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import jakarta.annotation.PostConstruct

@Service
class EventService(
    private val eventRepository: EventRepository,
    private val pythonParserService: PythonParserService
) {
    private val logger = LoggerFactory.getLogger(EventService::class.java)

    @Scheduled(fixedRate = 1800000) // Обновляем каждые 30 минут
    fun updateEvents() {
        try {
            logger.info("Начинаем обновление мероприятий")
            val events = pythonParserService.parseEvents()
            events.forEach { event ->
                eventRepository.save(event)
            }
            logger.info("Обновлено ${events.size} мероприятий")
        } catch (e: Exception) {
            logger.error("Ошибка при обновлении мероприятий: ${e.message}", e)
        }
    }

    // Запускаем первое обновление при старте
    @PostConstruct
    fun initialUpdate() {
        updateEvents()
    }
} 