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

@Service
class ActivityLogService(
    val activityLogRepository: ActivityLogRepository
) {

    fun addActivityLog(activityLog: ActivityLogDto) {
        activityLogRepository.save(activityLog.toEntity())
    }

    fun getActivityLogById(activityLogId: Long): ActivityLogEntity {
        return activityLogRepository.findByActivityLogId(activityLogId) ?: throw ActivityLogNotFoundException("Activity log with id $activityLogId not found")
    }

    fun getAllActivityLogsByUserId(userId: Long): ActivityLogEntity {
        return activityLogRepository.findAllByUserId(userId) ?: throw ActivityLogNotFoundException("Activity logs for user with id $userId not found")
    }

    fun getAllActivityLogsByAction(action: String): List<ActivityLogEntity> {
        return activityLogRepository.findAllByAction(action) ?: throw ActivityLogNotFoundException("Activity logs with action $action not found")
    }

    @Transactional
    fun deleteActivityLogById(activityLogId: Long): ActivityLogEntity {
        val activityLog = activityLogRepository.findByActivityLogId(activityLogId) ?: throw ActivityLogNotFoundException("Activity log with id $activityLogId not found")
        activityLogRepository.deleteByActivityLogId(activityLogId)
        return activityLog
    }

    @Transactional
    fun deleteAllActivityLogsByUserId(userId: Long): ActivityLogEntity {
        val activityLog = activityLogRepository.findAllByUserId(userId) ?: throw ActivityLogNotFoundException("Activity logs for user with id $userId not found")
        activityLogRepository.deleteAllByUserId(userId)
        return activityLog
    }

    @Transactional
    fun deleteAllActivityLogsByAction(action: String): List<ActivityLogEntity> {
        val activityLogs = activityLogRepository.deleteAllByAction(action) ?: throw ActivityLogNotFoundException("Activity logs with action $action not found")
        return activityLogs
    }

    @Transactional
    fun deleteAllActivityLogsOlderThan(date: LocalDateTime): List<ActivityLogEntity> {
        val activityLogs = activityLogRepository.deleteAllByCreatedAtBefore(date) ?: throw ActivityLogNotFoundException("Activity logs older than date $date not found")
        return activityLogs
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class ActivityLogNotFoundException(message: String) : RuntimeException(message)