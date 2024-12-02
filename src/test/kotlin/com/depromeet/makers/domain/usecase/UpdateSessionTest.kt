package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.session.Place
import com.depromeet.makers.domain.model.session.Session
import com.depromeet.makers.domain.model.session.SessionType
import com.depromeet.makers.domain.usecase.session.UpdateSession
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime


class UpdateSessionTest : BehaviorSpec({
    Given("세션 정보를 수정할 때") {
        val sessionGateway = mockk<SessionGateway>()
        val updateSession = UpdateSession(sessionGateway)

        val mockPreviousSession = Session(
            sessionId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 10,
            title = "세션 제목",
            description = "세션 설명",
            startTime = LocalDateTime.of(2030, 10, 1, 10, 0),
            sessionType = SessionType.OFFLINE,
            place = Place.emptyPlace(),
        )

        val mockSessionId = "123e4567-e89b-12d3-a456-426614174000"
        val mockTitle = "수정된 세션 제목"
        val mockDescription = "수정된 세션 설명"

        every { sessionGateway.getById(any()) } returns mockPreviousSession
        every { sessionGateway.save(any()) } returns (
                mockPreviousSession.copy(
                    title = mockTitle,
                    description = mockDescription,
                )
                )

        When("execute가 실행되면") {
            val result = updateSession.execute(
                UpdateSession.UpdateSessionInput(
                    sessionId = mockSessionId,
                    title = mockTitle,
                    description = mockDescription,
                    startTime = null,
                    sessionType = null,
                )
            )

            Then("수정된 세션 정보가 반환된다") {
                result.title shouldBe mockTitle
                result.description shouldBe mockDescription
            }
        }
    }
})
