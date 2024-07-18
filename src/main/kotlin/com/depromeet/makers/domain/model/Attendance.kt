package com.depromeet.makers.domain.model

import com.depromeet.makers.domain.exception.AttendanceAfterTimeException
import com.depromeet.makers.domain.exception.AttendanceAlreadyExistsException
import com.depromeet.makers.domain.exception.AttendanceBeforeTimeException
import com.depromeet.makers.util.generateULID
import java.time.LocalDateTime

data class Attendance(
    val attendanceId: String,
    val generation: Int,
    val week: Int,
    val member: Member,
    val sessionType: SessionType,
    val attendanceStatus: AttendanceStatus,
    val attendanceTime: LocalDateTime?,
    val tryCount: Int,
) {
    fun checkIn(now: LocalDateTime, attendanceStatus: AttendanceStatus): Attendance {
        return this.copy(
            attendanceStatus = attendanceStatus,
            attendanceTime = now
        )
    }

    fun expectAttendanceStatus(sessionStartTime: LocalDateTime, now: LocalDateTime): AttendanceStatus {
        return when {
            // [출석] 출석 대기 상태이며, 세션 시작 시간의 15분 전부터 15분 전까지 입니다. (정책에 따라 수정 필요)
            isAttendanceOnHold() && now.isAfter(sessionStartTime.minusMinutes(15)) && now.isBefore(sessionStartTime.plusMinutes(15)) -> AttendanceStatus.ATTENDANCE
            // [지각] 출석 대기 상태이며, 세션 시작 시간의 15분 후부터 30분 전까지 입니다. (정책에 따라 수정 필요)
            isAttendanceOnHold() && now.isAfter(sessionStartTime.plusMinutes(15)) && now.isBefore(sessionStartTime.plusMinutes(30)) -> AttendanceStatus.TARDY
            // [결석] 출석 대기 상태이며, 세션 시작 시간의 30분 후 입니다. (정책에 따라 수정 필요)
            isAttendanceOnHold() && now.isAfter(sessionStartTime.plusMinutes(30)) -> AttendanceStatus.ABSENCE
            else -> AttendanceStatus.ATTENDANCE_ON_HOLD
        }
    }

    fun isAvailableCheckInRequest(sessionStartTime: LocalDateTime, now: LocalDateTime): Boolean {
        when {
            attendanceStatus.isAttendanceOnHold().not() -> throw AttendanceAlreadyExistsException()
            now.isBefore(sessionStartTime.minusMinutes(15)) -> throw AttendanceBeforeTimeException()
            now.isAfter(sessionStartTime.plusMinutes(30)) -> throw AttendanceAfterTimeException()
            else -> return true
        }
    }

    fun isAttendanceOnHold() = attendanceStatus.isAttendanceOnHold()

    fun isAttendance() = attendanceStatus.isAttendance()

    fun isAbsence() = attendanceStatus.isAbsence()

    fun isTardy() = attendanceStatus.isTardy()

    fun tryCountUp() = this.copy(tryCount = tryCount + 1)

    fun isTryCountOver() = tryCount >= MAX_TRY_COUNT

    fun getTryCount() = TryCount(tryCount)

    fun update(
        attendanceId: String = this.attendanceId,
        generation: Int = this.generation,
        week: Int = this.week,
        member: Member = this.member,
        attendanceStatus: AttendanceStatus = this.attendanceStatus,
        attendanceTime: LocalDateTime? = this.attendanceTime,
        tryCount: Int = this.tryCount,
    ): Attendance {
        return this.copy(
            attendanceId = attendanceId,
            generation = generation,
            week = week,
            member = member,
            attendanceStatus = attendanceStatus,
            attendanceTime = attendanceTime,
            tryCount = tryCount
        )
    }

    companion object {
        const val MAX_TRY_COUNT = 3 // 최대 출석 코드 기입 횟수

        fun newAttendance(
            generation: Int,
            week: Int,
            member: Member,
            sessionType: SessionType = SessionType.ONLINE,
            attendance: AttendanceStatus = AttendanceStatus.ATTENDANCE_ON_HOLD
        ) = Attendance(
            attendanceId = generateULID(),
            generation = generation,
            week = week,
            member = member,
            sessionType = sessionType,
            attendanceStatus = attendance,
            attendanceTime = null,
            tryCount = 0
        )
    }

    data class TryCount(
        val tryCount: Int,
    )
}
