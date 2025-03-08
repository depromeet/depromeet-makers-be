package com.depromeet.makers.service

import com.depromeet.makers.domain.Test
import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import com.depromeet.makers.repository.TestRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TestService(
    private val testRepository: TestRepository,
) {
    fun getById(id: String): Test {
        return testRepository.findByIdOrNull(id) ?: throw DomainException(ErrorCode.TEST_NOT_FOUND)
    }
}
