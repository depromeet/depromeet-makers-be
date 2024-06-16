package com.depromeet.makers.infrastructure.gateway

import com.depromeet.makers.domain.gateway.NotificationGateway
import com.depromeet.makers.domain.model.Notification
import com.depromeet.makers.infrastructure.db.entity.NotificationEntity
import com.depromeet.makers.infrastructure.db.repository.JpaNotificationRepository
import org.springframework.stereotype.Component

@Component
class NotificationGatewayImpl(
    private val jpaNotificationRepository: JpaNotificationRepository,
) : NotificationGateway {

    override fun save(notification: Notification): Notification {
        val notificationEntity = NotificationEntity.fromDomain(notification)
        return jpaNotificationRepository
            .save(notificationEntity)
            .toDomain()
    }

    override fun findRecentNotificationByMemberId(memberId: String): Notification? {
        return jpaNotificationRepository
            .findFirstByMemberIdOrderByIdDesc(memberId)
            ?.toDomain()
    }

    override fun getById(notificationId: String): Notification {
        return jpaNotificationRepository
            .getReferenceById(notificationId)
            .let(NotificationEntity::toDomain)
    }
}
