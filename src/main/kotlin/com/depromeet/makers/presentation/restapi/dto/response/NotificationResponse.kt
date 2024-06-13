package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.model.NotificationType
import com.depromeet.makers.domain.usecase.GetRecentNotification
import io.swagger.v3.oas.annotations.media.Schema

data class NotificationResponse(

    @Schema(description = "알림 ID")
    val id: String,

    @Schema(description = "사용자 ID")
    val memberId: String,

    @Schema(description = "알림 내용")
    val content: String,

    @Schema(description = "알림 타입", example = "MEETING")
    val type: NotificationType,

    @Schema(description = "읽기 여부")
    val float: Boolean,
) {
    companion object {
        fun fromDomain(output: GetRecentNotification.GetRecentNotificationOutput) = NotificationResponse(
            id = output.id,
            memberId = output.memberId,
            content = output.content,
            type = output.type,
            float = output.float,
        )
    }
}
