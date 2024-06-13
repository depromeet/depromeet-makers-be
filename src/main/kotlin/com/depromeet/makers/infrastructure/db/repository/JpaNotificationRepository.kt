package com.depromeet.makers.infrastructure.db.repository

import com.depromeet.makers.infrastructure.db.entity.NotificationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaNotificationRepository : JpaRepository<NotificationEntity, String> {
    fun findFirstByMemberIdOrderByIdDesc(memberId: String): NotificationEntity?
}
