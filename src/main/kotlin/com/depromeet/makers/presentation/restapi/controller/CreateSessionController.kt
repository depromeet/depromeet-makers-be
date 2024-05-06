package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.CreateNewSession
import com.depromeet.makers.presentation.restapi.dto.request.CreateNewSessionRequest
import com.depromeet.makers.presentation.restapi.dto.response.CreateNewSessionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "세션 관련 API", description = "세션의 정보를 관리하는 API")
@RestController
@RequestMapping("/v1/sessions")
class CreateSessionController(
    private val createNewSession: CreateNewSession,
) {
    @Operation(summary = "새로운 세션 생성", description = "새로운 세션을 생성합니다.")
    // TODO: @PreAuthorize("hasRole('ADMIN')") MemberRole 필요.
    @PostMapping
    fun createNewSession(
        @RequestBody @Valid request: CreateNewSessionRequest,
    ): CreateNewSessionResponse {
        val session = createNewSession.execute(
            CreateNewSession.CreateNewSessionInput(
                generation = request.generation,
                week = request.week,
                title = request.title,
                description = request.description,
                startTime = request.startTime,
                sessionType = request.sessionType,
                address = request.address,
                longitude = request.longitude,
                latitude = request.latitude,
            )
        )
        return CreateNewSessionResponse.fromDomain(session)
    }
}
