package com.example.blank.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.ContentDto
import com.example.blank.entity.updateTimestamp
import com.example.blank.dto.toEntity
import com.example.blank.repository.ContentRepository
import com.example.blank.entity.ContentEntity
import java.util.Optional

@Service
class ContentService(
    val contentRepository: ContentRepository
) {

    fun addContent(content: ContentDto) {
        contentRepository.save(content.toEntity())
    }

    fun getContentById(id: Long): ContentEntity {
        return contentRepository.findById(id).orElseThrow { ContentNotFoundException("Content with id $id not found") }
    }

    fun getContentByTopicId(topicId: Long): List<ContentEntity> {
        return contentRepository.findAllByTopicId(topicId) ?: throw ContentNotFoundException("Content with topicId $topicId not found")
    }

    fun getAllContentByType(type: String): List<ContentEntity> {
        return contentRepository.findAllByType(type) ?: throw ContentNotFoundException("No content with type $type found")
    }

    @Transactional
    fun deleteContentById(id: Long): ContentEntity {
        val content = contentRepository.findById(id).orElseThrow { ContentNotFoundException("Content with id $id not found") }
        contentRepository.deleteById(id)
        return content
    }

    @Transactional
    fun deleteContentByTopicId(topicId: Long): Int {
        val contentList = contentRepository.findAllByTopicId(topicId) ?: throw ContentNotFoundException("Content with topicId $topicId not found")
        val deletedCount = contentRepository.deleteAllByTopicId(topicId)
        if (deletedCount <= 0) {
            throw ContentNotFoundException("Failed to delete content with topicId $topicId")
        }
        return deletedCount
    }

    @Transactional
    fun deleteAllContentByType(type: String): Int {
        val contentList = contentRepository.findAllByType(type) ?: throw ContentNotFoundException("No content with type $type found")
        val deletedCount = contentRepository.deleteAllByType(type)
        if (deletedCount <= 0) {
            throw ContentNotFoundException("Failed to delete content with type $type")
        }
        return deletedCount
    }

    @Transactional
    fun updateContentType(id: Long, newType: String): ContentEntity {
        val content = contentRepository.findById(id).orElseThrow { ContentNotFoundException("Content with id $id not found") }
        content.type = newType
        content.updateTimestamp()
        return contentRepository.save(content)
    }

    @Transactional
    fun updateContentData(id: Long, newData: String): ContentEntity {
        val content = contentRepository.findById(id).orElseThrow { ContentNotFoundException("Content with id $id not found") }
        content.contentData = newData
        content.updateTimestamp()
        return contentRepository.save(content)
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class ContentNotFoundException(message: String) : RuntimeException(message)