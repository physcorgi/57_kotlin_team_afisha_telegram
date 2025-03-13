package com.example.blank.repository

import com.example.blank.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
//import eu.vendeli.tgbot.types.User

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    override fun findById(id: Long): Optional<UserEntity>
    fun findByTelegramId(telegramId: Long): UserEntity?
    fun findAllByRating(rating: Int): List<UserEntity>?
    fun findAllByStreak(streak: Int): List<UserEntity>?
    
    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.id = :id")
    override fun deleteById(id: Long)
    
    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.telegramId = :telegramId")
    fun deleteByTelegramId(telegramId: Long): Int
}
