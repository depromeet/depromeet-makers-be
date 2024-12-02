package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.SessionAlreadyExistsException
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.session.SessionType
import com.depromeet.makers.domain.usecase.session.CreateNewSession
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldBeInteger
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class CreateNewSessionTest : BehaviorSpec({
    Given("온라인 세션을 등록할 때") {
        val sessionGateWay = mockk<SessionGateway>()
        val createNewSession = CreateNewSession(sessionGateWay)

        val mockGeneration = 14
        val mockWeek = 1
        val mockTitle = "오리엔테이션"
        val mockDescription = "온라인 세션의 첫 번째 주차입니다."
        val mockNullDescription = null
        val mockStartTime = LocalDateTime.now().plusDays(1)
        val mockSessionType = SessionType.ONLINE
        val mockAddress = null
        val mockLongitude = null
        val mockLatitude = null

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
                    longitude = mockLongitude,
                    latitude = mockLatitude,
                    placeName = "장소",
                )
            )
            Then("세션 정보가 등록된다.") {
                result.title shouldBeEqual mockTitle
                result.description shouldBe mockDescription
            }

            Then("4자리 랜덤 출석 코드가 생성된다.") {
                result.code shouldNotBe null
                result.code.shouldBeInteger()
                result.code!!.length shouldBe 4
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
                    longitude = mockLongitude,
                    latitude = mockLatitude,
                    placeName = "장소",
                )
            )
            Then("널 값의 description으로 세션 정보가 등록된다.") {
                result.title shouldBeEqual mockTitle
                result.description shouldBe mockNullDescription
            }
        }
    }

    Given("오프라인 세션을 등록할 때") {
        val sessionGateWay = mockk<SessionGateway>()
        val createNewSession = CreateNewSession(sessionGateWay)

        val mockGeneration = 14
        val mockWeek = 1
        val mockTitle = "오리엔테이션"
        val mockDescription = "오프라인 세션의 첫 번째 주차입니다."
        val mockStartTime = LocalDateTime.now().plusDays(1)
        val mockSessionType = SessionType.OFFLINE
        val mockAddress = "서울특별시 강남구 테헤란로 521"
        val mockLongitude = 37.507502
        val mockLatitude = 127.056344

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
                    longitude = mockLongitude,
                    latitude = mockLatitude,
                    placeName = "장소",
                )
            )
            Then("세션 정보가 등록된다.") {
                result.title shouldBeEqual mockTitle
                result.description shouldBe mockDescription
            }
        }
    }

    Given("이미 등록된 세션을 등록할 때") {
        val sessionGateWay = mockk<SessionGateway>()
        val createNewSession = CreateNewSession(sessionGateWay)

        val mockGeneration = 14
        val mockWeek = 1
        val mockTitle = "오리엔테이션"
        val mockDescription = "온라인 세션의 첫 번째 주차입니다."
        val mockStartTime = LocalDateTime.now().plusDays(1)
        val mockSessionType = SessionType.ONLINE
        val mockAddress = null
        val mockLongitude = null
        val mockLatitude = null

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
                        longitude = mockLongitude,
                        latitude = mockLatitude,
                        placeName = "장소",
                    )
                )
            }

            Then("SessionAlreadyExistsException 예외가 발생한다.") {
                shouldThrow<SessionAlreadyExistsException>(result)
            }
        }
    }
})
