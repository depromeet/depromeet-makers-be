package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.SessionAlreadyExistsException
import com.depromeet.makers.domain.gateway.SessionGateWay
import com.depromeet.makers.domain.model.SessionType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class CreateNewSessionTest : BehaviorSpec({
    Given("온라인 세션을 등록할 때") {
        val sessionGateWay = mockk<SessionGateWay>()
        val createNewSession = CreateNewSession(sessionGateWay)

        val mockGeneration = 14
        val mockWeek = 1
        val mockTitle = "오리엔테이션"
        val mockDescription = "온라인 세션의 첫 번째 주차입니다."
        val mockNullDescription = null
        val mockStartTime = LocalDateTime.now().plusDays(1)
        val mockSessionType = SessionType.ONLINE
        val mockAddress = null
        val mockX = null
        val mockY = null

        every { sessionGateWay.save(any()) } answers { firstArg() }
        every { sessionGateWay.existsByGenerationAndWeek(any(), any()) } answers { false }

        When("세션을 등록하면") {
            val result = createNewSession.execute(
                CreateNewSession.CreateNewSessionInput(
                    generation = mockGeneration,
                    week = mockWeek,
                    title = mockTitle,
                    description = mockDescription,
                    startTime = mockStartTime,
                    sessionType = mockSessionType,
                    address = mockAddress,
                    x = mockX,
                    y = mockY,
                )
            )
            Then("세션 정보가 등록된다.") {
                result.title shouldBeEqual mockTitle
                result.description shouldBe mockDescription
            }
        }

        When("널 값의 description으로 세션을 등록하면") {
            val result = createNewSession.execute(
                CreateNewSession.CreateNewSessionInput(
                    generation = mockGeneration,
                    week = mockWeek,
                    title = mockTitle,
                    description = mockNullDescription,
                    startTime = mockStartTime,
                    sessionType = mockSessionType,
                    address = mockAddress,
                    x = mockX,
                    y = mockY,
                )
            )
            Then("널 값의 description으로 세션 정보가 등록된다.") {
                result.title shouldBeEqual mockTitle
                result.description shouldBe mockNullDescription
            }
        }
    }

    Given("오프라인 세션을 등록할 때") {
        val sessionGateWay = mockk<SessionGateWay>()
        val createNewSession = CreateNewSession(sessionGateWay)

        val mockGeneration = 14
        val mockWeek = 1
        val mockTitle = "오리엔테이션"
        val mockDescription = "오프라인 세션의 첫 번째 주차입니다."
        val mockStartTime = LocalDateTime.now().plusDays(1)
        val mockSessionType = SessionType.OFFLINE
        val mockAddress = "서울특별시 강남구 테헤란로 521"
        val mockX = 37.507502
        val mockY = 127.056344

        every { sessionGateWay.save(any()) } answers { firstArg() }
        every { sessionGateWay.existsByGenerationAndWeek(any(), any()) } answers { false }

        When("세션을 등록하면") {
            val result = createNewSession.execute(
                CreateNewSession.CreateNewSessionInput(
                    generation = mockGeneration,
                    week = mockWeek,
                    title = mockTitle,
                    description = mockDescription,
                    startTime = mockStartTime,
                    sessionType = mockSessionType,
                    address = mockAddress,
                    x = mockX,
                    y = mockY,
                )
            )
            Then("세션 정보가 등록된다.") {
                result.title shouldBeEqual mockTitle
                result.description shouldBe mockDescription
            }
        }
    }

    Given("이미 등록된 세션을 등록할 때") {
        val sessionGateWay = mockk<SessionGateWay>()
        val createNewSession = CreateNewSession(sessionGateWay)

        val mockGeneration = 14
        val mockWeek = 1
        val mockTitle = "오리엔테이션"
        val mockDescription = "온라인 세션의 첫 번째 주차입니다."
        val mockStartTime = LocalDateTime.now().plusDays(1)
        val mockSessionType = SessionType.ONLINE
        val mockAddress = null
        val mockX = null
        val mockY = null

        every { sessionGateWay.save(any()) } answers {
            firstArg()
        }

        every { sessionGateWay.existsByGenerationAndWeek(any(), any()) } answers {
            true
        }

        When("이미 등록된 세션을 등록하면") {
            val result = {
                createNewSession.execute(
                    CreateNewSession.CreateNewSessionInput(
                        generation = mockGeneration,
                        week = mockWeek,
                        title = mockTitle,
                        description = mockDescription,
                        startTime = mockStartTime,
                        sessionType = mockSessionType,
                        address = mockAddress,
                        x = mockX,
                        y = mockY,
                    )
                )
            }

            Then("SessionAlreadyExistsException 예외가 발생한다.") {
                shouldThrow<SessionAlreadyExistsException>(result)
            }
        }
    }
})
