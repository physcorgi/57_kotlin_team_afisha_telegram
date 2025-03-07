package com.example.blank.repository

import com.example.blank.entity.UserTopicEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
//import eu.vendeli.tgbot.types.User

@Repository
interface UserTopicRepository : JpaRepository<UserTopicEntity, Long> {
    override fun findById(id: Long): Optional<UserTopicEntity>
    fun findAllByUserId(userId: Long): List<UserTopicEntity>?
    fun findAllByTopicId(topicId: Long): List<UserTopicEntity>?
    
    @Modifying
    @Query("DELETE FROM UserTopicEntity u WHERE u.id = :id")
    override fun deleteById(id: Long)
}
