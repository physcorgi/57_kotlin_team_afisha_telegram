package com.example.blank.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.UserTestResultDto
import com.example.blank.entity.updateTimestamp
import com.example.blank.dto.toEntity
import com.example.blank.repository.UserTestResultRepository
import com.example.blank.entity.UserTestResultEntity

@Service
class UserTestResultService(
    val userTestResultRepository: UserTestResultRepository
) {

    fun addUserTestResult(userTestResult: UserTestResultDto) {
        userTestResultRepository.save(userTestResult.toEntity())
    }

    fun getUserTestResultById(id: Long): UserTestResultEntity {
        return userTestResultRepository.findById(id)
            .orElseThrow { UserTestResultNotFoundException("User test result with id $id not found") }
    }

    fun getAllUserTestResultsByUserId(userId: Long): List<UserTestResultEntity> {
        return userTestResultRepository.findAllByUserId(userId)
            ?: throw UserTestResultNotFoundException("No test results found for user with id $userId")
    }

    fun getAllUserTestResultsByTestId(testId: Long): List<UserTestResultEntity> {
        return userTestResultRepository.findAllByTestId(testId)
            ?: throw UserTestResultNotFoundException("No test results found for test with id $testId")
    }

    fun getUserTestResultByUserIdAndTestId(userId: Long, testId: Long): UserTestResultEntity {
        return userTestResultRepository.findByUserIdAndTestId(userId, testId)
            ?: throw UserTestResultNotFoundException("No test result found for user $userId and test $testId")
    }

    fun getAllUserTestResultsByTestIdAndScore(testId: Long, score: Int): List<UserTestResultEntity> {
        return userTestResultRepository.findAllByTestIdAndScore(testId, score)
            ?: throw UserTestResultNotFoundException("No test results found for test $testId with score $score")
    }

    @Transactional
    fun deleteUserTestResultById(id: Long): UserTestResultEntity {
        val userTestResult = userTestResultRepository.findById(id)
            .orElseThrow { UserTestResultNotFoundException("User test result with id $id not found") }
        userTestResultRepository.deleteById(id)
        return userTestResult
    }

    @Transactional
    fun updateUserTestResultResult(id: Long, newScore: Int, newTime: Float): UserTestResultEntity {
        val userTestResult = userTestResultRepository.findById(id)
            .orElseThrow { UserTestResultNotFoundException("User test result with id $id not found") }
        userTestResult.score = newScore
        userTestResult.time = newTime
        userTestResult.count += 1
        userTestResult.updateTimestamp()
        return userTestResultRepository.save(userTestResult)
    }

//    @Transactional
//    fun updateUserTestResultScore(id: Long, newScore: Int): UserTestResultEntity {
//        val userTestResult = userTestResultRepository.findById(id)
//            .orElseThrow { UserTestResultNotFoundException("User test result with id $id not found") }
//        userTestResult.score = newScore
//        userTestResult.updateTimestamp()
//        return userTestResultRepository.save(userTestResult)
//    }
//
//    @Transactional
//    fun updateUserTestResultTime(id: Long, newTime: Float): UserTestResultEntity {
//        val userTestResult = userTestResultRepository.findById(id)
//            .orElseThrow { UserTestResultNotFoundException("User test result with id $id not found") }
//        userTestResult.time = newTime
//        userTestResult.updateTimestamp()
//        return userTestResultRepository.save(userTestResult)
//    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserTestResultNotFoundException(message: String) : RuntimeException(message)