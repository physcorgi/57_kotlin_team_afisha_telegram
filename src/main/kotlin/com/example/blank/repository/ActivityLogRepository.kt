package com.example.blank.repository

import com.example.blank.entity.ActivityLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

//import eu.vendeli.tgbot.types.User

@Repository
interface ActivityLogRepository : JpaRepository<ActivityLogEntity, Long> {
    override fun findById(id: Long): Optional<ActivityLogEntity>
    fun findAllByUserId(userId: Long): List<ActivityLogEntity>?
    fun findAllByAction(action: String): List<ActivityLogEntity>?
    
    @Modifying
    @Query("DELETE FROM ActivityLogEntity a WHERE a.id = :id")
    override fun deleteById(id: Long)

    @Modifying
    @Query("DELETE FROM ActivityLogEntity a WHERE a.userId = :userId")
    fun deleteAllByUserId(userId: Long): Int
    
    @Modifying
    @Query("DELETE FROM ActivityLogEntity a WHERE a.action = :action")
    fun deleteAllByAction(action: String): Int

    @Modifying
    @Query("DELETE FROM ActivityLogEntity a WHERE a.createdAt < :date")
    fun deleteAllByCreatedAtBefore(date: LocalDateTime): Int
}
