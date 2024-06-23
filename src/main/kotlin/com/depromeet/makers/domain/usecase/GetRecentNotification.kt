package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.NotificationGateway
import com.depromeet.makers.domain.model.Notification
import com.depromeet.makers.domain.model.NotificationType
import java.time.LocalDateTime

class GetRecentNotification(
    private val notificationGateway: NotificationGateway,
) : UseCase<GetRecentNotification.GetRecentNotificationInput, Notification> {

    data class GetRecentNotificationInput(
        val memberId: String,
    )

    override fun execute(input: GetRecentNotificationInput): Notification {
        // 조회 알림이 없다면 default 값으로 반환
        return notificationGateway.findRecentNotificationByMemberId(input.memberId)
            ?: return Notification(
                id = "defaultId",
                memberId = input.memberId,
                content = "defaultContent",
                type = NotificationType.NONE,
                createdAt = LocalDateTime.now(),
                readAt = LocalDateTime.now(),
            )
    }
}
