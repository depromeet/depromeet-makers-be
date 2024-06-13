package com.depromeet.makers.infrastructure.db.entity

import com.depromeet.makers.domain.model.Notification
import com.depromeet.makers.domain.model.NotificationType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class NotificationEntity private constructor(
    @Id
    @Column(name = "notification_id", columnDefinition = "CHAR(26)", nullable = false)
    val id: String,

    @Id
    @Column(name = "member_id", columnDefinition = "CHAR(26)", nullable = false)
    val memberId: String,

    @Column(name = "content", nullable = false)
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: NotificationType,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "read_at")
    var readAt: LocalDateTime?,
) {

    fun toDomain() = Notification(
        id = id,
        memberId = memberId,
        content = content,
        type = type,
        createdAt = createdAt,
        readAt = readAt,
    )

    companion object {
        fun fromDomain(notification: Notification) = NotificationEntity(
            id = notification.id,
            memberId = notification.memberId,
            content = notification.content,
            type = notification.type,
            createdAt = notification.createdAt,
            readAt = notification.readAt,
        )
    }
}
