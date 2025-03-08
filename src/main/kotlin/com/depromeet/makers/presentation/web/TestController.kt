package com.depromeet.makers.presentation.web

import com.depromeet.makers.presentation.web.dto.response.TestResponse
import com.depromeet.makers.service.TestService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val testService: TestService,
) {
    @GetMapping("/{id}")
    fun test(
        @PathVariable id: String
    ): TestResponse {
        val test = testService.getById(id)
        return TestResponse(
            name = test.name,
            age = test.age,
        )
    }
}
