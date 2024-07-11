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

class GetInfoSessionTest : BehaviorSpec({
    Given("MEMBER ROLE을 가진 유저가 오늘의 세션을 조회할 때") {
        val sessionGateway = mockk<SessionGateway>()
        val getSessions = GetInfoSession(sessionGateway)

        val mockSession = Session(
            sessionId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 1,
            title = "세션 제목",
            description = "세션 설명",
            startTime = LocalDateTime.of(2030, 10, 1, 10, 0),
            sessionType = SessionType.OFFLINE,
            place = Place.newPlace(
                address = "서울특별시 강남구 테헤란로 521",
                longitude = 127.034,
                latitude = 37.501,
                name = "장소"
            ),
        )
        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns mockSession

        When("execute가 실행되면") {
            val result = getSessions.execute(
                GetInfoSession.GetInfoSessionInput(
                    now = LocalDateTime.of(2030, 10, 1, 10, 0),
                    isOrganizer = false
                )
            )

            Then("위치 정보가 마스킹되어 반환된다") {
                result.place.longitude shouldBe 0.0
                result.place.latitude shouldBe 0.0
            }
        }
    }

    Given("ORGANIZER ROLE을 가진 유저가 오늘의 세션을 조회할 때") {
        val sessionGateway = mockk<SessionGateway>()
        val getSessions = GetInfoSession(sessionGateway)

        val mockSession = Session(
            sessionId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 1,
            title = "세션 제목",
            description = "세션 설명",
            startTime = LocalDateTime.of(2030, 10, 1, 10, 0),
            sessionType = SessionType.OFFLINE,
            place = Place.newPlace(
                address = "서울특별시 강남구 테헤란로 521",
                longitude = 127.034,
                latitude = 37.501,
                name = "장소"
            ),
        )
        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns mockSession

        When("execute가 실행되면") {
            val result = getSessions.execute(
                GetInfoSession.GetInfoSessionInput(
                    now = LocalDateTime.of(2030, 10, 1, 10, 0),
                    isOrganizer = true
                )
            )

            Then("위치 정보가 마스킹되지 않고 반환된다") {
                result.place.longitude shouldBe 127.034
                result.place.latitude shouldBe 37.501
            }
        }
    }
})
