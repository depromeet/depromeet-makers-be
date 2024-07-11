package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.InvalidCheckInDistanceException
import com.depromeet.makers.domain.exception.InvalidCheckInTimeException
import com.depromeet.makers.domain.exception.MissingPlaceParamException
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.domain.model.Place
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.model.SessionType
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class CheckInSessionTest : BehaviorSpec({
    Given("온라인 세션에 출석할 때") {
        val attendanceGateway = mockk<AttendanceGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val memberGateway = mockk<MemberGateway>()
        val checkInSession = CheckInSession(attendanceGateway, sessionGateway, memberGateway)

        val mockNow = LocalDateTime.of(2024, 5, 15, 16, 0)
        val mockMemberId = "123e4567-e89b-12d3-a456-426614174000"
        val mockLongitude = 127.0092
        val mockLatitude = 35.9418
        val mockMember = Member(
            memberId = "123e4567-e89b-12d3-a456-426614174000",
            name = "홍길동",
            email = "",
            passCord = null,
            generations = emptySet()
        )

        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns Session(
            sessionId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 1,
            title = "title",
            description = null,
            startTime = LocalDateTime.of(2024, 5, 15, 16, 0),
            sessionType = SessionType.ONLINE,
            place = Place.emptyPlace(),
        )

        every { memberGateway.getById(any()) } returns mockMember

        every { attendanceGateway.findByMemberIdAndGenerationAndWeek(any(), any(), any()) } returns Attendance.newAttendance(
            generation = 15,
            week = 1,
            member = mockMember,
            sessionType = SessionType.ONLINE,
        )

        every { attendanceGateway.save(any()) } returns mockk()

        When("execute가 실행되면") {
            val executor = {
                checkInSession.execute(
                    CheckInSession.CheckInSessionInput(
                        now = mockNow,
                        memberId = mockMemberId,
                        longitude = mockLongitude,
                        latitude = mockLatitude,
                    )
                )
            }

            Then("예외 없이 실행된다") {
                shouldNotThrowAny { executor() }
            }
        }
    }

    Given("출석 세션이 존재하지 않는 온라인 세션에 출석할 때") {
        val attendanceGateway = mockk<AttendanceGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val memberGateway = mockk<MemberGateway>()
        val checkInSession = CheckInSession(attendanceGateway, sessionGateway, memberGateway)

        val mockNow = LocalDateTime.of(2024, 6, 15, 16, 0)
        val mockMemberId = "123e4567-e89b-12d3-a456-426614174000"
        val mockLongitude = 127.0092
        val mockLatitude = 35.9418
        val mockMember = Member(
            memberId = "123e4567-e89b-12d3-a456-426614174000",
            name = "홍길동",
            email = "",
            passCord = null,
            generations = emptySet()
        )

        every { memberGateway.getById(any()) } returns mockMember

        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns null
        every { attendanceGateway.findByMemberIdAndGenerationAndWeek(any(), any(), any()) } returns mockk()
        every { attendanceGateway.save(any()) } returns mockk()

        When("execute가 실행되면") {
            val executor = {
                checkInSession.execute(
                    CheckInSession.CheckInSessionInput(
                        now = mockNow,
                        memberId = mockMemberId,
                        longitude = mockLongitude,
                        latitude = mockLatitude,
                    )
                )
            }

            Then("InvalidCheckInTimeException 예외를 던진다") {
                shouldThrow<InvalidCheckInTimeException> { executor() }
            }
        }
    }

    Given("오프라인 세션에 출석할 때") {
        val attendanceGateway = mockk<AttendanceGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val memberGateway = mockk<MemberGateway>()
        val checkInSession = CheckInSession(attendanceGateway, sessionGateway, memberGateway)

        val mockNow = LocalDateTime.of(2024, 5, 15, 16, 0)
        val mockMemberId = "123e4567-e89b-12d3-a456-426614174000"
        val mockLongitude = 127.0092
        val mockLatitude = 35.9418
        val mockMember = Member(
            memberId = "123e4567-e89b-12d3-a456-426614174000",
            name = "홍길동",
            email = "",
            passCord = null,
            generations = emptySet()
        )

        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns Session(
            sessionId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 1,
            title = "title",
            description = null,
            startTime = LocalDateTime.of(2024, 5, 15, 16, 0),
            sessionType = SessionType.OFFLINE,
            place = Place(
                address = "전북 익산시 부송동 100",
                name = "오프라인 장소",
                latitude = 35.9418,
                longitude = 127.0092,
            ),
        )

        every { memberGateway.getById(any()) } returns Member(
            memberId = "123e4567-e89b-12d3-a456-426614174000",
            name = "홍길동",
            email = "",
            passCord = null,
            generations = emptySet()
        )

        every { attendanceGateway.findByMemberIdAndGenerationAndWeek(any(), any(), any()) } returns Attendance.newAttendance(
            generation = 15,
            week = 1,
            member = mockMember,
            sessionType = SessionType.ONLINE,
        )
        every { attendanceGateway.save(any()) } returns mockk()

        When("execute가 실행되면") {
            val executor = {
                checkInSession.execute(
                    CheckInSession.CheckInSessionInput(
                        now = mockNow,
                        memberId = mockMemberId,
                        longitude = mockLongitude,
                        latitude = mockLatitude,
                    )
                )
            }

            Then("예외 없이 실행된다") {
                shouldNotThrowAny { executor() }
            }
        }
    }

    Given("위치에 벗어난 상태에서 오프라인 세션에 출석할 때") {
        val attendanceGateway = mockk<AttendanceGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val memberGateway = mockk<MemberGateway>()
        val checkInSession = CheckInSession(attendanceGateway, sessionGateway, memberGateway)

        val mockNow = LocalDateTime.of(2024, 5, 15, 16, 0)
        val mockMemberId = "123e4567-e89b-12d3-a456-426614174000"
        val mockLongitude = 10.0
        val mockLatitude = 10.0
        val mockMember = Member(
            memberId = "123e4567-e89b-12d3-a456-426614174000",
            name = "홍길동",
            email = "",
            passCord = null,
            generations = emptySet()
        )

        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns Session(
            sessionId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 1,
            title = "title",
            description = null,
            startTime = LocalDateTime.of(2024, 5, 15, 16, 0),
            sessionType = SessionType.OFFLINE,
            place = Place(
                address = "전북 익산시 부송동 100",
                name = "오프라인 장소",
                latitude = 35.9418,
                longitude = 127.0092,
            ),
        )

        every { memberGateway.getById(any()) } returns Member(
            memberId = "123e4567-e89b-12d3-a456-426614174000",
            name = "홍길동",
            email = "",
            passCord = null,
            generations = emptySet()
        )

        every { attendanceGateway.findByMemberIdAndGenerationAndWeek(any(), any(), any()) } returns Attendance.newAttendance(
            generation = 15,
            week = 1,
            member = mockMember,
            sessionType = SessionType.OFFLINE,

        )
        every { attendanceGateway.save(any()) } returns mockk()

        When("execute가 실행되면") {
            val executor = {
                checkInSession.execute(
                    CheckInSession.CheckInSessionInput(
                        now = mockNow,
                        memberId = mockMemberId,
                        longitude = mockLongitude,
                        latitude = mockLatitude,
                    )
                )
            }

            Then("InvalidCheckInDistanceException 예외를 던진다") {
                shouldThrow<InvalidCheckInDistanceException> { executor() }
            }
        }
    }

    Given("위치에 파라미터가 누락되어 오프라인 세션에 출석할 때") {
        val attendanceGateway = mockk<AttendanceGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val memberGateway = mockk<MemberGateway>()
        val checkInSession = CheckInSession(attendanceGateway, sessionGateway, memberGateway)

        val mockNow = LocalDateTime.of(2024, 5, 15, 16, 0)
        val mockMemberId = "123e4567-e89b-12d3-a456-426614174000"
        val mockLongitude = 0.0
        val mockLatitude = 0.0
        val mockMember = Member(
            memberId = "123e4567-e89b-12d3-a456-426614174000",
            name = "홍길동",
            email = "",
            passCord = null,
            generations = emptySet()
        )

        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns Session(
            sessionId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 1,
            title = "title",
            description = null,
            startTime = LocalDateTime.of(2024, 5, 15, 16, 0),
            sessionType = SessionType.OFFLINE,
            place = Place(
                address = "전북 익산시 부송동 100",
                name = "오프라인 장소",
                latitude = 35.9418,
                longitude = 127.0092,
            ),
        )

        every { memberGateway.getById(any()) } returns mockMember

        every { attendanceGateway.findByMemberIdAndGenerationAndWeek(any(), any(), any()) } returns Attendance.newAttendance(
            generation = 15,
            week = 1,
            member = mockMember,
            sessionType = SessionType.OFFLINE,
        )
        every { attendanceGateway.save(any()) } returns mockk()

        When("execute가 실행되면") {
            val executor = {
                checkInSession.execute(
                    CheckInSession.CheckInSessionInput(
                        now = mockNow,
                        memberId = mockMemberId,
                        longitude = mockLongitude,
                        latitude = mockLatitude,
                    )
                )
            }


            Then("MissingPlaceParamException 예외를 던진다") {
                shouldThrow<MissingPlaceParamException> { executor() }
            }
        }
    }
})
