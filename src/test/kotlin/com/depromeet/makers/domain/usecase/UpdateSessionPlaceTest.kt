package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.session.Place
import com.depromeet.makers.domain.model.session.Session
import com.depromeet.makers.domain.model.session.SessionType
import com.depromeet.makers.domain.usecase.session.UpdateSessionPlace
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class UpdateSessionPlaceTest : BehaviorSpec({
    Given("세션 장소를 수정할 때") {
        val sessionGateway = mockk<SessionGateway>()
        val updateSessionPlace = UpdateSessionPlace(sessionGateway)

        val mockPreviousSessionPlace = Place(
            address = "전북 익산시 부송동 100",
            name = "오프라인 장소",
            latitude = 35.9418,
            longitude = 127.0092,
        )
        val mockPreviousSession = Session(
            sessionId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 10,
            title = "세션 제목",
            description = "세션 설명",
            startTime = LocalDateTime.of(2030, 10, 1, 10, 0),
            sessionType = SessionType.OFFLINE,
            place = mockPreviousSessionPlace,
        )

        val mockSessionId = "123e4567-e89b-12d3-a456-426614174000"
        val mockAddress = "수정된 세션 장소"
        val mockLatitude = 50.0000
        val mockLongitude = 150.0000

        every { sessionGateway.getById(any()) } returns mockPreviousSession
        every { sessionGateway.save(any()) } returns (
                mockPreviousSession.copy(
                    place = Place(
                        address = mockAddress,
                        name = "오프라인 장소",
                        latitude = mockLatitude,
                        longitude = mockLongitude,
                    )
                )
                )

        When("execute가 실행되면") {
            val result = updateSessionPlace.execute(
                UpdateSessionPlace.UpdateSessionInput(
                    sessionId = mockSessionId,
                    address = mockAddress,
                    latitude = mockLatitude,
                    longitude = mockLongitude,
                    name = "오프라인 장소",
                )
            )

            Then("수정된 세션 정보가 반환된다") {
                result.place.address shouldBe mockAddress
                result.place.latitude shouldBe mockLatitude
                result.place.longitude shouldBe mockLongitude
            }
        }
    }
})
