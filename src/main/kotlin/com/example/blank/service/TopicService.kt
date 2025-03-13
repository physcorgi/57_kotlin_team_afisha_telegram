package com.example.blank.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.TopicDto
import com.example.blank.dto.toEntity
import com.example.blank.repository.TopicRepository
import com.example.blank.entity.TopicEntity
import com.example.blank.entity.updateTimestamp

@Service
class TopicService(
    val topicRepository: TopicRepository
) {

    fun addTopic(topic: TopicDto) {
        topicRepository.save(topic.toEntity())
    }

    fun getTopicById(id: Long): TopicEntity {
        return topicRepository.findById(id).orElseThrow { TopicNotFoundException("Topic with id $id not found") }
    }

    fun getTopicByName(name: String): TopicEntity {
        return topicRepository.findByName(name) ?: throw TopicNotFoundException("Topic with name $name not found")
    }

    fun getAllTopicsByCreatedBy(createdBy: String): List<TopicEntity> {
        return topicRepository.findAllByCreatedBy(createdBy) ?: throw TopicNotFoundException("No topics created by $createdBy found")
    }

    @Transactional
    fun deleteTopicById(id: Long): TopicEntity {
        val topic = topicRepository.findById(id).orElseThrow { TopicNotFoundException("Topic with id $id not found") }
        topicRepository.deleteById(id)
        return topic
    }

    @Transactional
    fun deleteTopicByName(name: String): Int {
        val topic = topicRepository.findByName(name) ?: throw TopicNotFoundException("Topic with name $name not found")
        val deletedCount = topicRepository.deleteByName(name)
        if (deletedCount <= 0) {
            throw TopicNotFoundException("Failed to delete topic with name $name")
        }
        return deletedCount
    }

    @Transactional
    fun deleteTopicsByCreatedBy(createdBy: String): Int {
        val topics = topicRepository.findAllByCreatedBy(createdBy) ?: throw TopicNotFoundException("No topics created by $createdBy found")

        if (topics.isEmpty()) {
            throw TopicNotFoundException("No topics created by $createdBy found")
        }

        val deletedCount = topicRepository.deleteAllByCreatedBy(createdBy)
        if (deletedCount <= 0) {
            throw TopicNotFoundException("Failed to delete topics created by $createdBy")
        }
        return deletedCount
    }

    @Transactional
    fun updateTopicName(id: Long, newName: String): TopicEntity {
        val topic = topicRepository.findById(id).orElseThrow { TopicNotFoundException("Topic with id $id not found") }
        topic.name = newName
        topic.updateTimestamp()
        return topicRepository.save(topic)
    }

    @Transactional
    fun updateTopicDescription(id: Long, newDescription: String): TopicEntity {
        val topic = topicRepository.findById(id).orElseThrow { TopicNotFoundException("Topic with id $id not found") }
        topic.description = newDescription
        topic.updateTimestamp()
        return topicRepository.save(topic)
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class TopicNotFoundException(message: String) : RuntimeException(message)