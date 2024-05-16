package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.InvalidCheckInTimeException
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.domain.model.AttendanceStatus
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
        }.getOrDefault(
            Attendance.newAttendance(
                generation = thisWeekSession.generation,
                week = thisWeekSession.week,
                member = member,
            )
        )

        // 현재 시간이 세션 시간 15분 전 && 세션 시작 시간 사이인지 확인 -> 팝업 띄우기 여부
        val isBeforeSession15minutes = input.now.isAfter(thisWeekSession.startTime.minusMinutes(15)) &&
                input.now.isBefore(thisWeekSession.startTime)

        // 현재 시간이 출석 요청 가능 시간 사이 && 출석 상태가 ON_HOLD인 경우 -> 플로팅 버튼 띄우기 여부
        val needFloatingButton = attendance.isAttendanceOnHold() &&
                input.now.isAfter(thisWeekSession.startTime) && input.now.isBefore(thisWeekSession.startTime.plusMinutes(120))

        val expectAttendanceStatus = when {
            // 세션 시작 ~ 30분 사이 -> 출석으로 인정될 상태
            input.now.isAfter(thisWeekSession.startTime) && input.now.isBefore(thisWeekSession.startTime.plusMinutes(30)) -> AttendanceStatus.ATTENDANCE
            // 세션 시작 30분 ~ 120분 사이 -> 지각으로 인정될 상태
            input.now.isAfter(thisWeekSession.startTime.plusMinutes(30)) &&
                    input.now.isBefore(thisWeekSession.startTime.plusMinutes(120)) -> AttendanceStatus.TARDY

            else -> AttendanceStatus.ATTENDANCE_ON_HOLD
        }

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
