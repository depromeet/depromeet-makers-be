package com.depromeet.makers.domain.usecase.attendence

import com.depromeet.makers.domain.exception.InvalidCheckInTimeException
import com.depromeet.makers.domain.exception.NotFoundAttendanceException
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.attendence.AttendanceStatus
import com.depromeet.makers.domain.usecase.UseCase
import java.time.DayOfWeek
import java.time.LocalDateTime

class GetCheckInStatus(
    private val memberGateway: MemberGateway,
    private val sessionGateway: SessionGateway,
    private val attendanceGateway: AttendanceGateway,
) : UseCase<GetCheckInStatus.GetCheckInStatusInput, GetCheckInStatus.GetCheckInStatusOutput> {
    data class GetCheckInStatusInput(
        val now: LocalDateTime,
        val memberId: String,
    )

    data class GetCheckInStatusOutput(
        val generation: Int,
        val week: Int,
        val isBeforeSession15minutes: Boolean,
        val needFloatingButton: Boolean,
        val expectAttendanceStatus: AttendanceStatus,
    )

    override fun execute(input: GetCheckInStatusInput): GetCheckInStatusOutput {
        val member = memberGateway.getById(input.memberId)

        val monday = input.now.getMonday()
        val thisWeekSession = sessionGateway.findByStartTimeBetween(
            monday,
            monday.plusDays(7)
        ) ?: throw InvalidCheckInTimeException()

        val attendance = runCatching {
            attendanceGateway.findByMemberIdAndGenerationAndWeek(
                member.memberId,
                thisWeekSession.generation,
                thisWeekSession.week
            )
        }.getOrElse { throw NotFoundAttendanceException() }

        // 현재 시간이 세션 시간 15분 전 && 세션 시작 시간 사이인지 확인 -> 팝업 띄우기 여부
        val isBeforeSession15minutes = input.now.isAfter(thisWeekSession.startTime.minusMinutes(15)) &&
                input.now.isBefore(thisWeekSession.startTime)

        // 현재 시간이 출석 요청 가능 시간 사이 && 출석 상태가 ON_HOLD(출석 대기) 인 경우 -> 플로팅 버튼 띄우기 여부
        val needFloatingButton = attendance.isAvailableCheckInRequest(thisWeekSession.startTime, input.now)

        val expectAttendanceStatus = attendance.expectAttendanceStatus(thisWeekSession.startTime, input.now)

        return GetCheckInStatusOutput(
            generation = attendance.generation,
            week = attendance.week,
            isBeforeSession15minutes = isBeforeSession15minutes,
            needFloatingButton = needFloatingButton,
            expectAttendanceStatus = expectAttendanceStatus,
        )
    }

    private fun LocalDateTime.getMonday() = this.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay()
}
