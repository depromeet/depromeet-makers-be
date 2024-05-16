package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.UpdateSession
import com.depromeet.makers.presentation.restapi.dto.request.UpdateSessionRequest
import com.depromeet.makers.presentation.restapi.dto.response.UpdateSessionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "세션 관련 API", description = "세션의 정보를 관리하는 API")
@RestController
@RequestMapping("/v1/sessions")
class UpdateSessionController(
    private val updateSession: UpdateSession,
) {
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
}
