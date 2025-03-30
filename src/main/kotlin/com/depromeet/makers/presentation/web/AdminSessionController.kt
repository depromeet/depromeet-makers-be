package com.depromeet.makers.presentation.web

import com.depromeet.makers.presentation.web.dto.request.SessionGenerationRequest
import com.depromeet.makers.presentation.web.dto.request.SessionRequest
import com.depromeet.makers.presentation.web.dto.response.SessionResponse
import com.depromeet.makers.service.SessionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.bson.types.ObjectId
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "[어드민] 세션 API", description = "세션 관련 API")
@RestController
@RequestMapping("/admin")
class AdminSessionController(
    private val sessionService: SessionService,
) {
    @Operation(summary = "[어드민] 세션 생성", description = "세션을 생성합니다.")
    @PostMapping("/v1/sessions")
    fun createSession(
        @RequestBody request: SessionRequest,
    ): SessionResponse {
        val session = sessionService.createSession(
            generation = request.generation,
            week = request.week,
            title = request.title,
            description = request.description,
            place = request.place,
            startTime = request.startTime,
            endTime = request.endTime,
        )
        return SessionResponse.from(session)
    }

    @Operation(summary = "[어드민] 세션 조회", description = "세션을 조회합니다.")
    @GetMapping("/v1/sessions/{sessionId}")
    fun getSession(
        authentication: Authentication,
        @PathVariable sessionId: String,
    ): SessionResponse {
        val session = sessionService.getSession(sessionId = ObjectId(sessionId))
        return SessionResponse.from(session)
    }

    @Operation(summary = "[어드민] 전체 세션 조회", description = "기수에 해당하는 세션을 조회합니다.")
    @GetMapping("/v1/sessions")
    fun getAllSessionByGeneration(
        authentication: Authentication,
        @RequestParam generation: Int,
    ): List<SessionResponse> {
        val sessions = sessionService.getAllSessionByGeneration(generation)

        return sessions.map { session -> SessionResponse.from(session) }
    }

    @Operation(summary = "[어드민] 세션 수정", description = "특정 세션을 수정합니다.")
    @PutMapping("/v1/sessions/{sessionId}")
    fun updateSession(
        @PathVariable sessionId: String,
        @RequestBody request: SessionRequest,
    ): SessionResponse {
        val session = sessionService.updateSession(
            sessionId = ObjectId(sessionId),
            generation = request.generation,
            week = request.week,
            title = request.title,
            description = request.description,
            place = request.place,
            startTime = request.startTime,
            endTime = request.endTime,
        )
        return SessionResponse.from(session)
    }

    @Operation(summary = "[어드민] 세션 코드 갱신", description = "특정 세션의 코드를 갱신합니다.")
    @PostMapping("/v1/sessions/{sessionId}/code")
    fun updateSessionCode(
        authentication: Authentication,
        @PathVariable sessionId: String,
    ): SessionResponse {
        val session = sessionService.updateSessionCode(sessionId = ObjectId(sessionId))
        return SessionResponse.from(session)
    }

    @Operation(summary = "[어드민] 세션 삭제", description = "특정 세션을 삭제합니다.")
    @DeleteMapping("/v1/sessions/{sessionId}")
    fun deleteSession(
        @PathVariable sessionId: String,
    ) {
        sessionService.deleteSession(ObjectId(sessionId))
    }

    @Operation(summary = "[어드민] 기수 별 세션 전체 삭제", description = "기수에 해당하는 모든 세션을 삭제합니다.")
    @DeleteMapping("/v1/sessions")
    fun deleteAllSessionByGeneration(
        @RequestBody request: SessionGenerationRequest,
    ) {
        sessionService.deleteAllByGeneration(request.generation)
    }
}
