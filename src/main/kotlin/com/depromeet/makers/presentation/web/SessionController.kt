package com.depromeet.makers.presentation.web

import com.depromeet.makers.presentation.web.dto.response.SessionResponse
import com.depromeet.makers.service.SessionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.bson.types.ObjectId
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "세션 API", description = "세션 관련 API")
@RestController
class SessionController(
    private val sessionService: SessionService,
) {
    @Operation(summary = "세션 조회", description = "세션을 조회합니다.")
    @GetMapping("/v1/sessions/{sessionId}")
    fun getSession(
        authentication: Authentication,
        @PathVariable sessionId: String,
    ): SessionResponse {
        val session = sessionService.getSession(sessionId = ObjectId(sessionId))
        return SessionResponse.from(session, mask = true)
    }

    @Operation(summary = "전체 세션 조회", description = "기수에 해당하는 세션을 조회합니다.")
    @GetMapping("/v1/sessions")
    fun getAllSessionByGeneration(
        authentication: Authentication,
        @RequestParam generation: Int,
    ): List<SessionResponse> {
        val sessions = sessionService.getAllSessionByGeneration(generation)

        return sessions.map { session -> SessionResponse.from(session, mask = true) }
    }
}
