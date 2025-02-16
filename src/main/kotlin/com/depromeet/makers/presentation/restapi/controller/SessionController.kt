package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.model.MemberRole
import com.depromeet.makers.domain.model.Place
import com.depromeet.makers.domain.usecase.CreateSession
import com.depromeet.makers.domain.usecase.DeleteSession
import com.depromeet.makers.domain.usecase.GetInfoSession
import com.depromeet.makers.domain.usecase.GetSessions
import com.depromeet.makers.domain.usecase.RefreshSessionCode
import com.depromeet.makers.domain.usecase.UpdateSession
import com.depromeet.makers.presentation.restapi.dto.request.GetSessionsRequest
import com.depromeet.makers.presentation.restapi.dto.request.SessionRequest
import com.depromeet.makers.presentation.restapi.dto.response.SessionsResponse
import com.depromeet.makers.presentation.restapi.dto.response.SessionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "세션 관련 API", description = "세션의 정보를 관리하는 API")
@RestController
@RequestMapping("/v1/sessions")
class SessionController(
    private val createSession: CreateSession,
    private val getSessions: GetSessions,
    private val updateSession: UpdateSession,
    private val deleteSession: DeleteSession,
    private val getInfoSession: GetInfoSession,
    private val refreshSessionCode: RefreshSessionCode,
) {
    @Operation(summary = "새로운 세션 생성", description = "새로운 세션을 생성합니다.")
    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping
    fun createNewSession(
        @RequestBody @Valid request: SessionRequest,
    ): SessionResponse {
        val place = request.place?.let {
            Place(
                placeId = it.placeId,
                name = it.placeName,
                address = it.address,
                longitude = it.longitude,
                latitude = it.latitude,
            )
        }

        val session = createSession.execute(
            CreateSession.CreateSessionInput(
                generation = request.generation,
                week = request.week,
                title = request.title,
                description = request.description,
                startTime = request.startTime,
                sessionType = request.sessionType,
                place = place
            )
        )
        return SessionResponse.fromDomain(session)
    }

    @Operation(
        summary = "기수에 따른 모든 주차의 세션들 조회 요청",
        description = "기수에 따른 모든 주차의 세션들을 조회합니다.\n" +
                "일반 유저의 경우 위도, 경도가 0.0 으로, code가 null로 마스킹되어 반환됩니다."
    )
    @Parameter(name = "generation", description = "조회할 세션의 기수", example = "15")
    @GetMapping
    fun getSessions(
        authentication: Authentication,
        @Valid request: GetSessionsRequest,
    ): SessionsResponse {
        val sessions = getSessions.execute(
            GetSessions.GetSessionsInput(
                generation = request.generation,
                isOrganizer = authentication.authorities.any { it.authority == MemberRole.ORGANIZER.roleName }
            )
        )

        return SessionsResponse(
            generation = request.generation,
            sessions = sessions.map { SessionResponse.fromDomain(it) },
        )
    }

    @Operation(
        summary = "세션 정보 조회", description = "세션의 정보를 조회합니다.\n" +
                "일반 유저의 경우 위도, 경도가 0.0 으로, code가 null로 마스킹되어 반환됩니다."
    )
    @GetMapping("/info")
    fun getInfoSession(
        authentication: Authentication,
    ): SessionResponse {
        return SessionResponse.fromDomain(
            getInfoSession.execute(
                GetInfoSession.GetInfoSessionInput(
                    now = LocalDateTime.now(),
                    isOrganizer = authentication.authorities.any { it.authority == MemberRole.ORGANIZER.roleName }
                )
            )
        )
    }

    @Operation(summary = "세션 정보 수정", description = "세션의 정보를 수정합니다.")
    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping("/{sessionId}")
    fun updateSession(
        @PathVariable sessionId: String,
        @RequestBody @Valid request: SessionRequest,
    ): SessionResponse {
        val place = request.place?.let {
            Place(
                placeId = it.placeId,
                name = it.placeName,
                address = it.address,
                longitude = it.longitude,
                latitude = it.latitude,
            )
        }

        val updatedSession = updateSession.execute(
            UpdateSession.UpdateSessionInput(
                sessionId = sessionId,
                generation = request.generation,
                week = request.week,
                title = request.title,
                description = request.description,
                startTime = request.startTime,
                sessionType = request.sessionType,
                place = place,
            )
        )
        return SessionResponse.fromDomain(updatedSession)
    }

    @Operation(summary = "세션 코드 갱신", description = "세션의 코드를 갱신합니다.")
    @PreAuthorize("hasRole('ORGANIZER')")
    @PatchMapping("/{sessionId}/code")
    fun refreshSessionCode(
        @PathVariable sessionId: String,
    ): SessionResponse {
        val updatedSession =
            refreshSessionCode.execute(
                sessionId = sessionId,
            )
        return SessionResponse.fromDomain(updatedSession)
    }

    @Operation(summary = "세션 삭제", description = "세션을 삭제합니다.")
    @PreAuthorize("hasRole('ORGANIZER')")
    @DeleteMapping("/{sessionId}")
    fun deleteSession(
        @Parameter(description = "세션 ID", required = true)
        @PathVariable sessionId: String,
    ) {
        deleteSession.execute(
            DeleteSession.DeleteSessionInput(
                sessionId = sessionId,
            )
        )
    }
}
