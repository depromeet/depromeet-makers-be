package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.DeleteSession
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "세션 관련 API", description = "세션의 정보를 관리하는 API")
@RestController
@RequestMapping("/v1/sessions")
class DeleteSessionController(
    private val deleteSession: DeleteSession,
) {
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
