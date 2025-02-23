package __kotlin_team_project.afisha.service

import __kotlin_team_project.afisha.model.Event
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import jakarta.annotation.PostConstruct
import java.io.File

@Service
class PythonParserService {
    private val logger = LoggerFactory.getLogger(PythonParserService::class.java)
    private val objectMapper = ObjectMapper().registerModule(JavaTimeModule())
    private val parserDir = ClassPathResource("parser").file.absolutePath
    private val parserScript = "$parserDir/afisha_parser.py"
    
    @PostConstruct
    fun setupPythonEnvironment() {
        try {
            logger.info("Настройка Python окружения")
            // Используем системный Python вместо виртуального окружения
            val process = ProcessBuilder("pip3", "install", "-r", "$parserDir/requirements.txt")
                .directory(File(parserDir))
                .redirectErrorStream(true)
                .start()

            val output = process.inputStream.bufferedReader().readText()
            val exitCode = process.waitFor()

            if (exitCode != 0) {
                logger.error("Ошибка при установке зависимостей Python: $output")
            } else {
                logger.info("Python зависимости успешно установлены")
            }
        } catch (e: Exception) {
            logger.error("Ошибка при настройке Python окружения: ${e.message}", e)
        }
    }
    
    fun parseEvents(): List<Event> {
        return try {
            logger.info("Запуск Python парсера")
            
            val process = ProcessBuilder("python3", parserScript)
                .directory(File(parserDir))
                .redirectErrorStream(false)
                .start()

            // Читаем stderr для логов
            Thread {
                process.errorStream.bufferedReader().useLines { lines ->
                    lines.forEach { logger.info("Python: $it") }
                }
            }.start()

            // Читаем stdout для JSON
            val output = process.inputStream.bufferedReader().readText()
            val exitCode = process.waitFor()
            
            if (exitCode != 0) {
                logger.error("Python скрипт завершился с ошибкой: $exitCode")
                return emptyList()
            }

            logger.info("Python скрипт успешно выполнен")
            objectMapper.readValue<List<Map<String, Any>>>(output).map { eventMap ->
                Event(
                    name = eventMap["name"] as String,
                    category = eventMap["category"] as String,
                    description = eventMap["description"] as String,
                    location = eventMap["location"] as String,
                    price = (eventMap["price"] as Number).toDouble(),
                    dateTime = LocalDateTime.parse(eventMap["dateTime"] as String)
                )
            }
        } catch (e: Exception) {
            logger.error("Ошибка при выполнении Python парсера: ${e.message}", e)
            emptyList()
        }
    }
} 