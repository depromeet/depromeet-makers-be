package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.InvalidCheckInCodeException
import com.depromeet.makers.domain.exception.NotSupportedCheckInCodeException
import com.depromeet.makers.domain.exception.TryCountOverException
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class CheckInSessionWithCodeTest : BehaviorSpec({
    Given("출석 코드로 오프라인 세션에 참여할 때") {
        val attendanceGateway = mockk<AttendanceGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val memberGateway = mockk<MemberGateway>()
        val checkInSessionWithCode = CheckInSessionWithCode(attendanceGateway, sessionGateway, memberGateway)

        val mockMemberId = "123e4567-e89b-12d3-a456-426614174000"
        val mockCode = "1234"
        val mockNow = LocalDateTime.of(2030, 10, 1, 10, 0)
        val mockMember = Member(
            memberId = "123e4567-e89b-12d3-a456-426614174000",
            name = "홍길동",
            email = "email@email.com",
            passCord = "1234",
            generations = emptySet(),
        )
        val mockAttendance = Attendance(
            attendanceId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 1,
            member = mockMember,
            sessionType = SessionType.OFFLINE,
            attendanceStatus = AttendanceStatus.ATTENDANCE_ON_HOLD,
            attendanceTime = null,
            tryCount = 0,
        )
        val mockSession = Session(
            sessionId = mockMemberId,
            generation = 15,
            week = 1,
            title = "세션 제목",
            description = "세션 설명",
            startTime = mockNow,
            sessionType = SessionType.OFFLINE,
            place = Place.newPlace(
                address = "서울특별시 강남구 테헤란로 521",
                longitude = 127.034,
                latitude = 37.501,
                name = "장소"
            ),
            code = mockCode,
        )

        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns mockSession
        every { memberGateway.getById(mockMemberId) } returns mockMember
        every { attendanceGateway.findByMemberIdAndGenerationAndWeek(mockMemberId, any(), any()) } returns mockAttendance
        every { attendanceGateway.save(any()) } returns mockAttendance.checkIn(mockNow, AttendanceStatus.ATTENDANCE)

        When("올바른 출석 코드를 입력하면") {
            val result = checkInSessionWithCode.execute(
                CheckInSessionWithCode.CheckInSessionWithCodeInput(
                    mockMemberId,
                    mockNow,
                    mockCode,
                )
            )

            Then("출석 성공 정보가 반환된다.") {
                result.attendanceStatus shouldBe AttendanceStatus.ATTENDANCE
                result.attendanceTime shouldBe mockNow
            }
        }

        When("잘못된 출석 코드를 입력하면") {
            val executor = {
                checkInSessionWithCode.execute(
                    CheckInSessionWithCode.CheckInSessionWithCodeInput(
                        mockMemberId,
                        mockNow,
                        "0000",
                    )
                )
            }

            Then("출석 실패 정보가 반환된다.") {
                shouldThrow<InvalidCheckInCodeException>(executor)
            }
        }
    }

    Given("출석 코드로 온라인 세션에 참여할 때") {
        val attendanceGateway = mockk<AttendanceGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val memberGateway = mockk<MemberGateway>()
        val checkInSessionWithCode = CheckInSessionWithCode(attendanceGateway, sessionGateway, memberGateway)

        val mockMemberId = "123e4567-e89b-12d3-a456-426614174000"
        val mockCode = "1234"
        val mockNow = LocalDateTime.of(2030, 10, 1, 10, 0)

        every { memberGateway.getById(any()) } returns mockk<Member>()
        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns mockk<Session>() {
            every { isOnline() } returns true
        }

        When("출석 요청하면") {
            val executor = {
                checkInSessionWithCode.execute(
                    CheckInSessionWithCode.CheckInSessionWithCodeInput(
                        mockMemberId,
                        mockNow,
                        mockCode,
                    )
                )
            }

            Then("온라인 세션에는 출석할 수 없다.") {
                shouldThrow<NotSupportedCheckInCodeException>(executor)
            }
        }
    }

    Given("재시도 횟수를 초과하여, 출석 코드로 오프라인 세션에 참여할 때") {
        val attendanceGateway = mockk<AttendanceGateway>()
        val sessionGateway = mockk<SessionGateway>()
        val memberGateway = mockk<MemberGateway>()
        val checkInSessionWithCode = CheckInSessionWithCode(attendanceGateway, sessionGateway, memberGateway)

        val mockMemberId = "123e4567-e89b-12d3-a456-426614174000"
        val mockCode = "1234"
        val mockNow = LocalDateTime.of(2030, 10, 1, 10, 0)
        val mockMember = Member(
            memberId = "123e4567-e89b-12d3-a456-426614174000",
            name = "홍길동",
            email = "email@email.com",
            passCord = "1234",
            generations = emptySet(),
        )
        val mockAttendance = Attendance(
            attendanceId = "123e4567-e89b-12d3-a456-426614174000",
            generation = 15,
            week = 1,
            member = mockMember,
            sessionType = SessionType.OFFLINE,
            attendanceStatus = AttendanceStatus.ATTENDANCE_ON_HOLD,
            attendanceTime = null,
            tryCount = Attendance.MAX_TRY_COUNT,
        )
        val mockSession = Session(
            sessionId = mockMemberId,
            generation = 15,
            week = 1,
            title = "세션 제목",
            description = "세션 설명",
            startTime = mockNow,
            sessionType = SessionType.OFFLINE,
            place = Place.newPlace(
                address = "서울특별시 강남구 테헤란로 521",
                longitude = 127.034,
                latitude = 37.501,
                name = "장소"
            ),
            code = mockCode,
        )

        every { sessionGateway.findByStartTimeBetween(any(), any()) } returns mockSession
        every { memberGateway.getById(mockMemberId) } returns mockMember
        every { attendanceGateway.findByMemberIdAndGenerationAndWeek(mockMemberId, any(), any()) } returns mockAttendance

        When("출석 요청하면") {
            val executor = {
                checkInSessionWithCode.execute(
                    CheckInSessionWithCode.CheckInSessionWithCodeInput(
                        mockMemberId,
                        mockNow,
                        "0000",
                    )
                )
            }

            Then("재시도 횟수 초과 예외가 발생한다.") {
                shouldThrow<TryCountOverException>(executor)
            }
        }
    }
})
