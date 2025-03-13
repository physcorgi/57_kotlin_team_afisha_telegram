package com.example.blank.entity

import com.example.blank.dto.TestDto
import jakarta.persistence.*
import java.time.LocalDateTime
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(
    name = "Tests",
    indexes = [
        Index(columnList = "id", name = "test_id_hidx"),
        Index(columnList = "content_type", name = "content_type_hidx")
    ]
)
class TestEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "content_type", nullable = false, length = 50)
    val contentType: String = "",

    @Column(nullable = true, length = 50)
    var difficulty: String? = null,

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    var questions: String = "[]",

    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    var answers: String = "[]",

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // Конструктор по умолчанию для Hibernate
    constructor() : this(0, "", null, "[]", "[]", LocalDateTime.now(), LocalDateTime.now())
}

fun TestEntity.updateTimestamp() {
    updatedAt = LocalDateTime.now()
}

fun TestEntity.toDto() = TestDto(
    contentType = contentType,
    difficulty = difficulty,
    questions = questions,
    answers = answers,
    createdAt = createdAt,
    updatedAt = updatedAt
)