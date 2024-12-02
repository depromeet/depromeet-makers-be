package com.depromeet.makers.presentation.restapi.dto.notification.response

import com.depromeet.makers.domain.model.notification.Notification
import com.depromeet.makers.domain.model.notification.NotificationType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "알림 DTO")
data class NotificationResponse(

    @Schema(description = "알림 ID", example = "01HWPNRE5TS9S7VC99WPETE5KE")
    val id: String,

    @Schema(description = "사용자 ID", example = "01HWPNRE5TS9S7VC99WPETE5KE")
    val memberId: String,

    @Schema(description = "알림 내용", example = "지난주에 결석하셨군요. 증빙이 필요하다면 증빙서류를 제출해주세요.")
    val content: String,

    @Schema(description = "알림 타입", example = "DOCUMENT")
    val type: NotificationType,

    @Schema(description = "읽기 여부", example = "false")
    val isRead: Boolean,
) {
    companion object {
        fun fromDomain(output: Notification): NotificationResponse {
            return NotificationResponse(
                id = output.id,
                memberId = output.memberId,
                content = output.content,
                type = output.type,
                isRead = output.readAt != null,
            )
        }
    }
}
