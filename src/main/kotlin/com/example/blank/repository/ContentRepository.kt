package com.example.blank.repository

import com.example.blank.entity.ContentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
//import eu.vendeli.tgbot.types.User

@Repository
interface ContentRepository : JpaRepository<ContentEntity, Long> {
    override fun findById(id: Long): Optional<ContentEntity>
    fun findAllByTopicId(topicId: Long): List<ContentEntity>?
    fun findAllByType(type: String): List<ContentEntity>?
    
    @Modifying
    @Query("DELETE FROM ContentEntity c WHERE c.topicId = :topicId")
    fun deleteAllByTopicId(topicId: Long): Int
    
    @Modifying
    @Query("DELETE FROM ContentEntity c WHERE c.type = :type")
    fun deleteAllByType(type: String): Int
}
