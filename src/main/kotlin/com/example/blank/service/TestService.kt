package com.example.blank.service

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.TestDto
import com.example.blank.dto.toEntity
import com.example.blank.repository.TestRepository
import com.example.blank.entity.TestEntity
import com.example.blank.entity.updateTimestamp

@Service
class TestService(
    val testRepository: TestRepository
) {

    fun addTest(test: TestDto) {
        testRepository.save(test.toEntity())
    }

    fun getTestById(id: Long): TestEntity {
        return testRepository.findById(id).orElseThrow { TestNotFoundException("Test with id $id not found") }
    }

    fun getAllTestsByContentType(contentType: String): List<TestEntity> {
        return testRepository.findAllByContentType(contentType) ?: throw TestNotFoundException("No tests with contentType $contentType found")
    }

    fun getAllTestsByDifficulty(difficulty: String): List<TestEntity> {
        return testRepository.findAllByDifficulty(difficulty) ?: throw TestNotFoundException("No tests with difficulty $difficulty found")
    }

    @Transactional
    fun deleteTestById(id: Long): TestEntity {
        val test = testRepository.findById(id).orElseThrow { TestNotFoundException("No test with id $id found") }
        testRepository.deleteById(id)
        return test
    }

    @Transactional
    fun deleteTestsByContentType(contentType: String): Int {
        val tests = testRepository.findAllByContentType(contentType) ?: throw TestNotFoundException("No test with contentType $contentType found")

        if (tests.isEmpty()) {
            throw TestNotFoundException("No tests with contentType $contentType found")
        }

        return testRepository.deleteAllByContentType(contentType)
    }

    @Transactional
    fun deleteTestsByDifficulty(difficulty: String): Int {
        val tests = testRepository.findAllByDifficulty(difficulty) ?: throw TestNotFoundException("No tests with difficulty $difficulty found")

        if (tests.isEmpty()) {
            throw TestNotFoundException("No tests with difficulty $difficulty found")
        }

        return testRepository.deleteAllByDifficulty(difficulty)
    }

    @Transactional
    fun updateTestDifficulty(id: Long, newDifficulty: String): TestEntity {
        val test = testRepository.findById(id).orElseThrow { TestNotFoundException("No test with id $id found") }
        test.difficulty = newDifficulty
        test.updateTimestamp()
        return testRepository.save(test)
    }

    @Transactional
    fun updateTestQuestions(id: Long, newQuestions: String): TestEntity {
        val test = testRepository.findById(id).orElseThrow { TestNotFoundException("Test with id $id not found") }
        test.questions = newQuestions
        test.updateTimestamp()
        return testRepository.save(test)
    }

    @Transactional
    fun updateTestAnswers(id: Long, newAnswers: String): TestEntity {
        val test = testRepository.findById(id).orElseThrow { TestNotFoundException("Test with id $id not found") }
        test.answers = newAnswers
        test.updateTimestamp()
        return testRepository.save(test)
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class TestNotFoundException(message: String) : RuntimeException(message)