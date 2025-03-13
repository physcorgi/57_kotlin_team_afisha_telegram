package com.example.blank.entity

import com.example.blank.dto.UserTopicDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "User_Topics",
    indexes = [
        Index(columnList = "id", name = "user_topic_id_hidx"),
        Index(columnList = "user_id", name = "user_id_hidx"),
        Index(columnList = "topic_id", name = "topic_id_hidx")
    ]
)
class UserTopicEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long = 0,

    @Column(name = "topic_id", nullable = false)
    val topicId: Long = 0,

    @Column(name = "selected_at", nullable = false)
    val selectedAt: LocalDateTime = LocalDateTime.now()
) {
    // Конструктор по умолчанию для Hibernate
    constructor() : this(0, 0, 0, LocalDateTime.now())
}

fun UserTopicEntity.toDto() = UserTopicDto(
    userId = userId,
    topicId = topicId,
    selectedAt = selectedAt
)