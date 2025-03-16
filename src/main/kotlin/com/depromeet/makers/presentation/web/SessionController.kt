package com.depromeet.makers.presentation.web

import com.depromeet.makers.config.properties.AppProperties
import com.depromeet.makers.domain.Session
import com.depromeet.makers.domain.enums.SessionType
import com.depromeet.makers.domain.vo.Code
import com.depromeet.makers.domain.vo.SessionPlace
import com.depromeet.makers.service.SessionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.bson.types.ObjectId
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Tag(name = "세션 API", description = "세션 관련 API")
@RestController
class SessionController(
    private val appProperties: AppProperties,
    private val sessionService: SessionService,
) {
    @Operation(summary = "[어드민] 세션 생성", description = "세션을 생성합니다.")
    @PreAuthorize("hasRole('ADMIN')")
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

    @Operation(summary = "세션 조회", description = "세션을 조회합니다.")
    @GetMapping("/v1/sessions/{sessionId}")
    fun getSession(
        authentication: Authentication,
        @PathVariable sessionId: String,
    ): SessionResponse {
        val needMask = !authentication.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN"))
        val session = sessionService.getSession(
            sessionId = ObjectId(sessionId),
        )
        return SessionResponse.from(session, needMask)
    }

    @Operation(summary = "전체 세션 조회", description = "기수에 해당하는 세션을 조회합니다.")
    @GetMapping("/v1/sessions/generation/{generation}/")
    fun getAllSessionByGeneration(
        authentication: Authentication,
        @PathVariable generation: Int = appProperties.depromeet.generation,
    ): List<SessionResponse> {
        val needMask = !authentication.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN"))
        val sessions = sessionService.getAllSessionByGeneration(
            generation = generation,
        )
        return sessions.map { session -> SessionResponse.from(session, needMask) }
    }

    @Operation(summary = "기수 & 주차 별 세션 조회", description = "기수와 주차에 해당하는 세션을 조회합니다.")
    @GetMapping("/v1/sessions/generation/{generation}/week/{week}")
    fun getSessionByWeek(
        authentication: Authentication,
        @PathVariable generation: Int = appProperties.depromeet.generation,
        @PathVariable week: Int,
    ): SessionResponse {
        val needMask = !authentication.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN"))
        val session = sessionService.getSession(
            generation = generation,
            week = week,
        )
        return SessionResponse.from(session, needMask)
    }

    @Operation(summary = "[어드민] 세션 수정", description = "특정 세션을 수정합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/v1/sessions/{sessionId}")
    fun updateSession(
        @PathVariable sessionId: String,
        @RequestBody request: SessionRequest,
    ): SessionResponse {
        val session = sessionService.updateSession(
            sessionId = ObjectId(sessionId),
            title = request.title,
            description = request.description,
            place = request.place,
            startTime = request.startTime,
            endTime = request.endTime,
        )
        return SessionResponse.from(session)
    }

    @Operation(summary = "[어드민] 세션 코드 갱신", description = "특정 세션의 코드를 갱신합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/v1/sessions/{sessionId}/code")
    fun updateSessionCode(
        authentication: Authentication,
        @PathVariable sessionId: String,
    ): SessionResponse {
        val session = sessionService.updateSessionCode(
            sessionId = ObjectId(sessionId),
        )
        return SessionResponse.from(session)
    }

    @Operation(summary = "[어드민] 세션 삭제", description = "특정 세션을 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/v1/sessions/{sessionId}")
    fun deleteSession(
        @PathVariable sessionId: String,
    ) {
        sessionService.deleteSession(ObjectId(sessionId))
    }

    @Operation(summary = "[어드민] 기수 별 세션 전체 삭제", description = "기수에 해당하는 모든 세션을 삭제합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/v1/sessions/generation/{generation}")
    fun deleteAllSessionByGeneration(
        @PathVariable generation: Int,
    ) {
        sessionService.deleteAllByGeneration(generation)
    }
}

data class SessionRequest(
    val generation: Int,
    val week: Int,
    val title: String,
    val description: String,
    val place: SessionPlace?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
)

data class SessionResponse(
    val sessionId: String,
    val generation: Int,
    val week: Int,
    val title: String,
    val description: String,
    val type: SessionType,
    val place: SessionPlace?,
    val code: Code,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
) {
    companion object {
        fun from(session: Session): SessionResponse {
            return SessionResponse(
                sessionId = session.id.toHexString(),
                generation = session.generation,
                week = session.week,
                title = session.title,
                description = session.description,
                type = session.getType(),
                place = session.place,
                startTime = session.startTime,
                code = session.code,
                endTime = session.endTime,
            )
        }

        fun from(session: Session, mask: Boolean): SessionResponse {
            return when (mask) {
                false -> from(session)
                true -> from(session).copy(
                    code = session.code.mask()
                )
            }
        }
    }
}
