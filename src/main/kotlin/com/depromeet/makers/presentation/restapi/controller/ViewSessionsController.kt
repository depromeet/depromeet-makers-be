package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.ViewSessions
import com.depromeet.makers.presentation.restapi.dto.request.ViewSessionsRequest
import com.depromeet.makers.presentation.restapi.dto.response.ViewSessionsResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "세션 관련 API", description = "세션의 정보를 관리하는 API")
@RestController
@RequestMapping("/v1/sessions")
class ViewSessionsController(
    private val viewSessions: ViewSessions,
) {
    @Operation(summary = "기수에 따른 모든 주차의 세션들 조회 요청", description = "기수에 따른 모든 주차의 세션들을 조회합니다.")
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping
    @ResponseBody
    fun viewSessions(
        @Valid request: ViewSessionsRequest,
    ): ViewSessionsResponse {
        val sessions = viewSessions.execute(
            ViewSessions.ViewSessionsInput(
                generation = request.generation,
            )
        ).map { ViewSessionsResponse.SessionResponse.fromDomain(it) }

        return ViewSessionsResponse(
            generation = request.generation,
            sessions = sessions,
        )
    }
}
