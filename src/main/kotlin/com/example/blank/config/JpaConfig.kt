package com.example.blank.config

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

/**
 * Конвертер для полей с типом JSONB в H2
 * Используется для сохранения JSON в обычное текстовое поле в H2
 */
@Configuration
@Profile("dev") // Применяем только для dev-профиля с H2
class JpaConfig {
    companion object {
        private val objectMapper = ObjectMapper()
    }
    
    /**
     * Конвертер для JSON строк, который используется в H2 вместо JSONB
     */
    @jakarta.persistence.Converter(autoApply = true)
    class JsonConverter : AttributeConverter<String, String> {
        override fun convertToDatabaseColumn(attribute: String?): String? {
            return attribute
        }

        override fun convertToEntityAttribute(dbData: String?): String? {
            return dbData
        }
    }
} 