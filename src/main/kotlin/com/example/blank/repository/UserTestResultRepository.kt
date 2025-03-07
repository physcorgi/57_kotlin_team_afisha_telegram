package com.example.blank.repository

import com.example.blank.entity.UserTestResultEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional
//import eu.vendeli.tgbot.types.User

@Repository
interface UserTestResultRepository : JpaRepository<UserTestResultEntity, Long> {
    override fun findById(id: Long): Optional<UserTestResultEntity>
    fun findAllByUserId(userId: Long): List<UserTestResultEntity>?
    fun findAllByTestId(testId: Long): List<UserTestResultEntity>?
    fun findByUserIdAndTestId(userId: Long, testId: Long): UserTestResultEntity?
    fun findAllByTestIdAndScore(testId: Long, score: Int): List<UserTestResultEntity>?
    
    @Modifying
    @Query("DELETE FROM UserTestResultEntity u WHERE u.id = :id")
    override fun deleteById(id: Long)
}
