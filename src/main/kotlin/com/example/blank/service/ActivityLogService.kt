package com.example.blank.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.ActivityLogDto
import com.example.blank.entity.updateTimestamp
import com.example.blank.dto.toEntity
import com.example.blank.repository.ActivityLogRepository
import com.example.blank.entity.ActivityLogEntity
import java.time.LocalDateTime
import java.util.*

@Service
class ActivityLogService(
    val activityLogRepository: ActivityLogRepository
) {

    fun addActivityLog(activityLog: ActivityLogDto) {
        activityLogRepository.save(activityLog.toEntity())
    }

    fun getActivityLogById(id: Long): Optional<ActivityLogEntity> {
        return activityLogRepository.findById(id) ?: throw ActivityLogNotFoundException("Activity log with id $id not found")
    }

    fun getAllActivityLogsByUserId(userId: Long): List<ActivityLogEntity> {
        return activityLogRepository.findAllByUserId(userId) ?: throw ActivityLogNotFoundException("Activity logs for user with id $userId not found")
    }

    fun getAllActivityLogsByAction(action: String): List<ActivityLogEntity> {
        return activityLogRepository.findAllByAction(action) ?: throw ActivityLogNotFoundException("Activity logs with action $action not found")
    }

    @Transactional
    fun deleteActivityLogById(id: Long): Optional<ActivityLogEntity> {
        val activityLog = activityLogRepository.findById(id) ?: throw ActivityLogNotFoundException("Activity log with id $id not found")
        activityLogRepository.deleteById(id)
        return activityLog
    }

    @Transactional
    fun deleteAllActivityLogsByUserId(userId: Long): List<ActivityLogEntity> {
        val activityLogs = activityLogRepository.findAllByUserId(userId) ?: throw ActivityLogNotFoundException("Activity logs for user with id $userId not found")
        activityLogRepository.deleteAllByUserId(userId)
        return activityLogs
    }

    @Transactional
    fun deleteAllActivityLogsByAction(action: String): List<ActivityLogEntity> {
        val activityLogs = activityLogRepository.findAllByAction(action) ?: throw ActivityLogNotFoundException("Activity logs with action $action not found")
        activityLogRepository.deleteAllByAction(action)
        return activityLogs
    }

    @Transactional
    fun deleteAllActivityLogsOlderThan(date: LocalDateTime): Int {
        val deletedCount = activityLogRepository.deleteAllByCreatedAtBefore(date)
        if (deletedCount == 0) {
            throw ActivityLogNotFoundException("Activity logs older than date $date not found")
        }
        return deletedCount
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class ActivityLogNotFoundException(message: String) : RuntimeException(message)