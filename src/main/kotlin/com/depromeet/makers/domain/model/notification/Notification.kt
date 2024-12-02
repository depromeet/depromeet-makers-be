package com.depromeet.makers.domain.model.notification

import com.depromeet.makers.util.generateULID
import java.time.LocalDateTime

data class Notification(
    val id: String,
    val memberId: String,
    val content: String,
    val type: NotificationType,
    val createdAt: LocalDateTime,
    val readAt: LocalDateTime?,
) {
    fun read(now: LocalDateTime) = copy(readAt = now)

    companion object {
        fun newNotification(
            memberId: String,
            content: String,
            type: NotificationType,
            createdAt: LocalDateTime
        ): Notification {
            return Notification(
                id = generateULID(),
                memberId = memberId,
                content = content,
                type = type,
                createdAt = createdAt,
                readAt = null,
            )
        }
    }
}
