package com.depromeet.makers.domain.model

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
    fun read() = copy(readAt = LocalDateTime.now())

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
