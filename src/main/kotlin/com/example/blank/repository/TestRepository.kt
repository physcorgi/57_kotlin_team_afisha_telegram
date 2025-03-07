package com.example.blank.repository

import com.example.blank.entity.TestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
//import eu.vendeli.tgbot.types.User

@Repository
interface TestRepository : JpaRepository<TestEntity, Long> {
    override fun findById(id: Long): Optional<TestEntity>
    fun findAllByContentType(contentType: String): List<TestEntity>?
    fun findAllByDifficulty(difficulty: String): List<TestEntity>?
    
    @Modifying
    @Query("DELETE FROM TestEntity t WHERE t.id = :id")
    override fun deleteById(id: Long)
    
    @Modifying
    @Query("DELETE FROM TestEntity t WHERE t.contentType = :contentType")
    fun deleteAllByContentType(contentType: String): Int
    
    @Modifying
    @Query("DELETE FROM TestEntity t WHERE t.difficulty = :difficulty")
    fun deleteAllByDifficulty(difficulty: String): Int
}
