package com.example.blank.repository

import com.example.blank.entity.TopicEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
//import eu.vendeli.tgbot.types.User

@Repository
interface TopicRepository : JpaRepository<TopicEntity, Long> {
    override fun findById(id: Long): Optional<TopicEntity>
    fun findByName(name: String): TopicEntity?
    fun findAllByCreatedBy(createdBy: String): List<TopicEntity>?
    
    @Modifying
    @Query("DELETE FROM TopicEntity t WHERE t.id = :id")
    override fun deleteById(id: Long)
    
    @Modifying
    @Query("DELETE FROM TopicEntity t WHERE t.name = :name")
    fun deleteByName(name: String): Int
    
    @Modifying
    @Query("DELETE FROM TopicEntity t WHERE t.createdBy = :createdBy")
    fun deleteAllByCreatedBy(createdBy: String): Int
}
