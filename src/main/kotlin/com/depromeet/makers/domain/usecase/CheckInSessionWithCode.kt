package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.*
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.NotificationGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Attendance
import java.time.DayOfWeek
import java.time.LocalDateTime

class CheckInSessionWithCode(
    private val attendanceGateway: AttendanceGateway,
    private val sessionGateway: SessionGateway,
    private val memGateway: NotificationGateway,
) : UseCase<CheckInSessionWithCode.CheckInSessionWithCodeInput, Attendance> {
    data class CheckInSessionWithCodeInput(
        val memberId: String,
        val now: LocalDateTime,
        val code: String,
    )

    override fun execute(input: CheckInSessionWithCodeInput): Attendance {
        val member = memGateway.getById(input.memberId)
        val monday = input.now.getMonday()

        val thisWeekSession =
            sessionGateway.findByStartTimeBetween(
                monday,
                monday.plusDays(7),
            ) ?: throw InvalidCheckInTimeException()

        if (thisWeekSession.isOnline()) {
            throw NotSupportedCheckInCodeException()
        }

        val attendance =
            runCatching {
                attendanceGateway.findByMemberIdAndGenerationAndWeek(
                    member.memberId,
                    thisWeekSession.generation,
                    thisWeekSession.week,
                )
            }.getOrElse { throw NotFoundAttendanceException() }

        if (attendance.isTryCountOver()) {
            throw TryCountOverException()
        }

        attendance.isAvailableCheckInRequest(thisWeekSession.startTime, input.now)

        if (thisWeekSession.code != input.code) {
            attendanceGateway.save(attendance.tryCountUp())
            throw InvalidCheckInCodeException(attendance.getTryCount())
        }

        val expectAttendanceStatus = attendance.expectAttendanceStatus(thisWeekSession.startTime, input.now)
        return attendanceGateway.save(attendance.checkIn(input.now, expectAttendanceStatus))
    }

    private fun LocalDateTime.getMonday() = this.toLocalDate().with(DayOfWeek.MONDAY).atStartOfDay()
}
