package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Place
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.model.SessionType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldBeInteger
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class RefreshSessionCodeTest :
    BehaviorSpec({
        Given("세션 코드를 갱신할 때") {
            val sessionGateway = mockk<SessionGateway>()
            val refreshSessionCode = RefreshSessionCode(sessionGateway = sessionGateway)

            val previousCode = "previousCode"
            val mockSession =
                Session(
                    sessionId = "123e4567-e89b-12d3-a456-426614174000",
                    generation = 15,
                    week = 1,
                    title = "세션 제목",
                    description = "세션 설명",
                    startTime = LocalDateTime.of(2030, 10, 1, 10, 0),
                    sessionType = SessionType.OFFLINE,
                    place =
                        Place.newPlace(
                            name = "테스트 장소",
                            address = "전북 익산시 부송동 100",
                            longitude = 35.9418,
                            latitude = 126.9544,
                        ),
                    code = previousCode,
                )

            every { sessionGateway.getById(any()) } returns mockSession
            every { sessionGateway.save(any()) } returns mockSession.update(code = Session.generateCode())

            When("execute가 실행되면") {
                val result =
                    refreshSessionCode.execute(
                        sessionId = mockSession.sessionId,
                    )

                Then("세션 코드가 갱신된 세션이 반환된다") {
                    result.code shouldNotBe previousCode
                    result.code shouldNotBe null
                    result.code.shouldBeInteger()
                    result.code!!.length shouldBe 4
                }
            }
        }
    })
