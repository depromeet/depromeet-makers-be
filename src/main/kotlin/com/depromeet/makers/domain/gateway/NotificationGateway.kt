package com.depromeet.makers.domain.gateway

import com.depromeet.makers.domain.model.Notification

interface NotificationGateway {
    fun getById(notificationId: String): Notification
    fun save(notification: Notification): Notification
    fun findRecentNotificationByMemberId(memberId: String): Notification?
}
