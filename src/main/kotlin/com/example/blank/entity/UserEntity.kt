package com.example.blank.entity

import com.example.blank.dto.UserDto
import jakarta.persistence.*
import java.time.LocalDateTime
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(
    name = "Users",
    indexes = [
        Index(columnList = "id", name = "user_id_hidx"),
        Index(columnList = "telegram_id", name = "telegram_id_hidx")
    ]
)
class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "telegram_id", nullable = false, unique = true)
    val telegramId: Long = 0,

    @Column(nullable = false, length = 255)
    var username: String = "",

    @Column(name = "full_name", nullable = true, length = 255)
    var fullName: String? = null,

    @Column(name = "profile_data", nullable = true)
    @JdbcTypeCode(SqlTypes.JSON)
    var profileData: String? = null,

    @Column(nullable = false)
    var rating: Int = 0,

    @Column(nullable = false)
    var streak: Int = 0,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // Конструктор по умолчанию для Hibernate
    constructor() : this(0, 0, "", null, null, 0, 0, LocalDateTime.now(), LocalDateTime.now())
}

fun UserEntity.updateTimestamp() {
        updatedAt = LocalDateTime.now()
    }

fun UserEntity.toDto() = UserDto(
    telegramId = telegramId,
    username = username,
    fullName = fullName,
    profileData = profileData,
    rating = rating,
    streak = streak,
    createdAt = createdAt,
    updatedAt = updatedAt
)