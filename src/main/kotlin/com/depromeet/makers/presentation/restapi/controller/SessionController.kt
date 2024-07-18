package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.model.MemberRole
import com.depromeet.makers.domain.usecase.*
import com.depromeet.makers.presentation.restapi.dto.request.CreateNewSessionRequest
import com.depromeet.makers.presentation.restapi.dto.request.GetSessionsRequest
import com.depromeet.makers.presentation.restapi.dto.request.UpdateSessionPlaceRequest
import com.depromeet.makers.presentation.restapi.dto.request.UpdateSessionRequest
import com.depromeet.makers.presentation.restapi.dto.response.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Tag(name = "세션 관련 API", description = "세션의 정보를 관리하는 API")
@RestController
@RequestMapping("/v1/sessions")
class SessionController(
    private val createNewSession: CreateNewSession,
    private val getSessions: GetSessions,
    private val updateSession: UpdateSession,
    private val updateSessionPlace: UpdateSessionPlace,
    private val deleteSession: DeleteSession,
    private val getInfoSession: GetInfoSession,
) {
    @Operation(summary = "새로운 세션 생성", description = "새로운 세션을 생성합니다.")
    @PreAuthorize("hasRole('ORGANIZER')")
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
                placeName = request.placeName,
            )
        )
        return CreateNewSessionResponse.fromDomain(session)
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
    ): GetSessionsResponse {
        val sessions = getSessions.execute(
            GetSessions.GetSessionsInput(
                generation = request.generation,
                isOrganizer = authentication.authorities.any { it.authority == MemberRole.ORGANIZER.roleName }
            )
        ).map { GetSessionsResponse.SessionResponse.fromDomain(it) }

        return GetSessionsResponse(
            generation = request.generation,
            sessions = sessions,
        )
    }

    @Operation(
        summary = "세션 정보 조회", description = "세션의 정보를 조회합니다.\n" +
                "일반 유저의 경우 위도, 경도가 0.0 으로, code가 null로 마스킹되어 반환됩니다."
    )
    @GetMapping("/info")
    fun getInfoSession(
        authentication: Authentication,
    ): GetSessionResponse {
        return GetSessionResponse.fromDomain(
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
        @RequestBody @Valid request: UpdateSessionRequest,
    ): UpdateSessionResponse {
        val updatedSession = updateSession.execute(
            UpdateSession.UpdateSessionInput(
                sessionId = sessionId,
                title = request.title,
                description = request.description,
                startTime = request.startTime,
                sessionType = request.sessionType,
            )
        )
        return UpdateSessionResponse.fromDomain(updatedSession)
    }

    @Operation(summary = "세션 장소 수정", description = "세션의 장소를 수정합니다.")
    @PreAuthorize("hasRole('ORGANIZER')")
    @PutMapping("/{sessionId}/place")
    fun updateSessionPlace(
        @PathVariable sessionId: String,
        @RequestBody @Valid request: UpdateSessionPlaceRequest,
    ): UpdateSessionPlaceResponse {
        val updatedSession = updateSessionPlace.execute(
            UpdateSessionPlace.UpdateSessionInput(
                sessionId = sessionId,
                address = request.address,
                latitude = request.latitude,
                longitude = request.longitude,
                name = request.placeName,
            )
        )
        return UpdateSessionPlaceResponse.fromDomain(updatedSession)
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
