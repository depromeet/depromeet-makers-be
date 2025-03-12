package com.depromeet.makers.service

import com.depromeet.makers.domain.Test
import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import com.depromeet.makers.domain.vo.Age
import com.depromeet.makers.repository.TestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TestService(
    private val testRepository: TestRepository,
) {
    fun create(name: String, age: Age): Test {
        return testRepository.save(Test(name = name, age = age))
    }

    fun getById(id: String): Test {
        return testRepository.findByIdOrNull(id) ?: throw DomainException(ErrorCode.TEST_NOT_FOUND)
    }
}
