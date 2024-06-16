package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.NotificationGateway
import com.depromeet.makers.domain.model.NotificationType
import java.time.LocalDateTime

class GetRecentNotification(
    private val notificationGateway: NotificationGateway,
) : UseCase<GetRecentNotification.GetRecentNotificationInput, GetRecentNotification.GetRecentNotificationOutput> {

    data class GetRecentNotificationInput(
        val memberId: String,
    )

    data class GetRecentNotificationOutput(
        val id: String,
        val memberId: String,
        val content: String,
        val type: NotificationType,
        val createdAt: LocalDateTime,
        val isRead: Boolean = false,
    )

    override fun execute(input: GetRecentNotificationInput): GetRecentNotificationOutput {
        val notification = notificationGateway.findRecentNotificationByMemberId(input.memberId)

        // 조회 알림이 없다면 default 값
        if (notification == null) {
            return GetRecentNotificationOutput(
                id = "defaultId",
                memberId = input.memberId,
                content = "defaultContent",
                type = NotificationType.NONE,
                createdAt = LocalDateTime.now(),
                isRead = true,
            )
        }

        return GetRecentNotificationOutput(
            id = notification.id,
            memberId = notification.memberId,
            content = notification.content,
            type = notification.type,
            createdAt = notification.createdAt,
            isRead = notification.readAt != null,
        )
    }
}
