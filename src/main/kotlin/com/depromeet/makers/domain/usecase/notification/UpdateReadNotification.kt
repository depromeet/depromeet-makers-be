package com.depromeet.makers.domain.usecase.notification

import com.depromeet.makers.domain.gateway.NotificationGateway
import com.depromeet.makers.domain.model.notification.Notification
import com.depromeet.makers.domain.usecase.UseCase
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
