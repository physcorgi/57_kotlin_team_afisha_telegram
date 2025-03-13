package com.example.blank.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.UserDto
import com.example.blank.entity.updateTimestamp
import com.example.blank.dto.toEntity
import com.example.blank.repository.UserRepository
import com.example.blank.entity.UserEntity

@Service
class UserService(
    val userRepository: UserRepository
) {

    fun addUser(user: UserDto) {
        userRepository.save(user.toEntity())
    }

    fun getUserById(id: Long): UserEntity {
        return userRepository.findById(id).orElseThrow { UserNotFoundException("User with id $id not found") }
    }

    fun getUserByTelegramId(telegramId: Long): UserEntity {
        return userRepository.findByTelegramId(telegramId) ?: throw UserNotFoundException("User with telegramId $telegramId not found")
    }

    fun getAllUsersByRating(rating: Int): List<UserEntity> {
        return userRepository.findAllByRating(rating) ?: throw UserNotFoundException("No users with rating $rating found")
    }

    fun getAllUsersByStreak(streak: Int): List<UserEntity> {
        return userRepository.findAllByStreak(streak) ?: throw UserNotFoundException("No users with streak $streak found")
    }

    @Transactional
    fun deleteUserById(id: Long): UserEntity {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User with id $id not found") }
        userRepository.deleteById(id)
        return user
    }

    @Transactional
    fun deleteUserByTelegramId(telegramId: Long): Int {
        val user = userRepository.findByTelegramId(telegramId) ?: throw UserNotFoundException("User with telegramId $telegramId not found")
        val deletedCount = userRepository.deleteByTelegramId(telegramId)
        if (deletedCount <= 0) {
            throw UserNotFoundException("Failed to delete user with telegramId $telegramId")
        }
        return deletedCount
    }

    @Transactional
    fun updateUserUsername(id: Long, newUsername: String): UserEntity {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User with id $id not found") }
        user.username = newUsername
        user.updateTimestamp()
        return userRepository.save(user)
    }

    @Transactional
    fun updateUserFullName(id: Long, newFullName: String): UserEntity {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User with id $id not found") }
        user.fullName = newFullName
        user.updateTimestamp()
        return userRepository.save(user)
    }

    @Transactional
    fun updateUserProfileData(id: Long, newProfileData: String): UserEntity {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User with id $id not found") }
        user.profileData = newProfileData
        user.updateTimestamp()
        return userRepository.save(user)
    }

    @Transactional
    fun updateUserRating(id: Long, newRating: Int): UserEntity {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User with id $id not found") }
        user.rating = newRating
        user.updateTimestamp()
        return userRepository.save(user)
    }

    @Transactional
    fun updateUserStreak(id: Long, newStreak: Int): UserEntity {
        val user = userRepository.findById(id).orElseThrow { UserNotFoundException("User with id $id not found") }
        user.streak = newStreak
        user.updateTimestamp()
        return userRepository.save(user)
    }

}

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(message: String) : RuntimeException(message)