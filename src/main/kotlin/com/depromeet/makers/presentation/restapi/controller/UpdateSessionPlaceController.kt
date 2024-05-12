package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.UpdateSessionPlace
import com.depromeet.makers.presentation.restapi.dto.request.UpdateSessionPlaceRequest
import com.depromeet.makers.presentation.restapi.dto.response.UpdateSessionPlaceResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "세션 관련 API", description = "세션의 정보를 관리하는 API")
@RestController
@RequestMapping("/v1/sessions")
class UpdateSessionPlaceController(
    private val updateSessionPlace: UpdateSessionPlace,
) {
    @Operation(summary = "세션 장소 수정", description = "세션의 장소를 수정합니다.")
    @PreAuthorize("hasRole('ORGANIZER')")
    @PostMapping("/{sessionId}/place")
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
            )
        )
        return UpdateSessionPlaceResponse.fromDomain(updatedSession)
    }
}
