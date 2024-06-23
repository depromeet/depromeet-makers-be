package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.NotificationGateway
import com.depromeet.makers.domain.model.Notification
import java.time.LocalDateTime

class UpdateReadNotification(
    private val notificationGateway: NotificationGateway,
) : UseCase<UpdateReadNotification.UpdateReadNotificationInput, Notification> {

    data class UpdateReadNotificationInput(
        val notificationId: String,
        val memberId: String,
        val now: LocalDateTime = LocalDateTime.now(),
    )

    override fun execute(input: UpdateReadNotificationInput): Notification {
        val notification = notificationGateway.getById(input.notificationId)

        return notificationGateway.save(notification.read(input.now))
    }
}
