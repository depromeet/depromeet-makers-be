package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.GetRecentNotification
import com.depromeet.makers.presentation.restapi.dto.response.NotificationResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "알림 API", description = "알림 관련 API")
@RestController
@RequestMapping("/v1/notifications")
class NotificationController(
    private val getRecentNotification: GetRecentNotification,
) {
    @Operation(summary = "최근 알림 조회", description = "로그인한 사용자의 최근 알림을 조회합니다.")
    @GetMapping
    fun getRecentNotification(
        authentication: Authentication,
    ): NotificationResponse {
        return NotificationResponse.fromDomain(
            getRecentNotification.execute(
                GetRecentNotification.GetRecentNotificationInput(
                    memberId = authentication.name,
                )
            )
        )
    }
}
