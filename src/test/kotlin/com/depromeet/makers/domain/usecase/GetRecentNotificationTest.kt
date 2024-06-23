package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.NotificationGateway
import com.depromeet.makers.domain.model.Notification
import com.depromeet.makers.domain.model.NotificationType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class GetRecentNotificationTest : BehaviorSpec({
    Given("읽지 않은 최근 알림이 존재하는 경우") {
        val notificationGateway = mockk<NotificationGateway>()
        val getRecentNotification = GetRecentNotification(notificationGateway)

        val mockMemberId = "hasNotificationMemberId"

        val mockNewNotification = Notification.newNotification(
            memberId = mockMemberId,
            content = "알림 내용",
            type = NotificationType.DOCUMENT,
            createdAt = LocalDateTime.now(),
        )
        every { notificationGateway.findRecentNotificationByMemberId(any()) } returns mockNewNotification

        When("최근 알림을 조회하면") {
            val result = getRecentNotification.execute(
                GetRecentNotification.GetRecentNotificationInput(
                    memberId = mockMemberId,
                )
            )

            Then("읽지 않은 알림이 조회된다") {
                result.readAt shouldBe null
            }
        }
    }

    Given("읽은 최근 알림이 존재하는 경우") {
        val notificationGateway = mockk<NotificationGateway>()
        val getRecentNotification = GetRecentNotification(notificationGateway)

        val mockMemberId = "hasNotificationMemberId"

        val mockNewNotification = Notification(
            id = "123e4567-e89b-12d3-a456-426614174000",
            memberId = mockMemberId,
            content = "알림 내용",
            type = NotificationType.DOCUMENT,
            createdAt = LocalDateTime.now(),
            readAt = LocalDateTime.now(),
        )
        every { notificationGateway.findRecentNotificationByMemberId(any()) } returns mockNewNotification

        When("최근 알림을 조회하면") {
            val result = getRecentNotification.execute(
                GetRecentNotification.GetRecentNotificationInput(
                    memberId = mockMemberId,
                )
            )

            Then("읽은 알림이 조회된다") {
                result.readAt shouldNotBe null
            }
        }
    }
})
