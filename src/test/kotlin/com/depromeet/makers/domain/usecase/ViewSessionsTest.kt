package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Place
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.model.SessionType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class ViewSessionsTest : BehaviorSpec({
    Given("기수 별 세션을 모두 조회할 때") {
        val sessionGateway = mockk<SessionGateway>()
        val viewSessions = ViewSessions(sessionGateway)

        val mockShuffledSessionList = (1..16).map { week ->
            Session(
                sessionId = "123e4567-e89b-12d3-a456-426614174000",
                generation = 15,
                week = week,
                title = "세션 제목",
                description = "세션 설명",
                startTime = LocalDateTime.of(2030, 10, 1, 10, 0),
                sessionType = SessionType.OFFLINE,
                place = Place.emptyPlace(),
                attendanceMemberIds = emptySet(),
            )
        }.toList().shuffled()

        every { sessionGateway.findAllByGeneration(any()) } returns mockShuffledSessionList

        When("execute가 실행되면") {
            val result = viewSessions.execute(
                ViewSessions.ViewSessionsInput(
                    generation = 15,
                )
            )

            Then("16주차 세션 정보가 반환된다") {
                result.size shouldBe 16
            }
            Then("주차별로 정렬되어 반환된다.") {
                result shouldBe mockShuffledSessionList.sortedBy { it.week }
            }
        }
    }
})
