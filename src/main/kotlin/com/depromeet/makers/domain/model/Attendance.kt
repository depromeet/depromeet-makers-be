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
    val attendanceStatus: AttendanceStatus,
    val attendanceTime: LocalDateTime?
) {
    fun checkIn(attendanceTime: LocalDateTime, sessionStartTime: LocalDateTime): Attendance {
        if (!isAttendanceOnHold()) {
            throw AttendanceAlreadyExistsException()
        }
        sessionStartTime.isAvailableCheckInTime(attendanceTime)

        return this.copy(
            attendanceStatus = attendanceTime.checkIn(sessionStartTime),
            attendanceTime = LocalDateTime.now()
        )
    }

    fun isAttendanceOnHold() = attendanceStatus.isAttendanceOnHold()

    fun isAttendance() = attendanceStatus.isAttendance()

    fun isAbsence() = attendanceStatus.isAbsence()

    fun isTardy() = attendanceStatus.isTardy()

    private fun LocalDateTime.isAvailableCheckInTime(sessionStartTime: LocalDateTime): Boolean {
        // 출석 요청 가능 시간은 세션 시작 시간의 30분 전부터 120분 후까지 입니다. (정책에 따라 수정 필요)
        if (this.isBefore(sessionStartTime.minusMinutes(15))) {
            throw AttendanceBeforeTimeException()
        }
        if (this.isAfter(sessionStartTime.plusMinutes(240))) {
            throw AttendanceAfterTimeException()
        }
        return true
    }

    private fun LocalDateTime.checkIn(sessionStartTime: LocalDateTime): AttendanceStatus {
        return when {
            // 출석 가능 시간은 세션 시작 시간의 15분 전부터 15분 후까지 입니다. (정책에 따라 수정 필요)
            this.isAfter(sessionStartTime.minusMinutes(15)) && this.isBefore(sessionStartTime.plusMinutes(15)) -> AttendanceStatus.ATTENDANCE
            // 지각은 세션 시작 시간의 15분 후부터 30분 후까지 입니다. (정책에 따라 수정 필요)
            this.isAfter(sessionStartTime.plusMinutes(15)) && this.isBefore(sessionStartTime.plusMinutes(30)) -> AttendanceStatus.TARDY
            // 결석은 세션 시작 시간의 30분 후 입니다. (정책에 따라 수정 필요)
            this.isAfter(sessionStartTime.plusMinutes(30)) -> AttendanceStatus.ABSENCE
            else -> throw AttendanceAfterTimeException()
        }

    }

    fun update(
        attendanceId: String = this.attendanceId,
        generation: Int = this.generation,
        week: Int = this.week,
        member: Member = this.member,
        attendanceStatus: AttendanceStatus = this.attendanceStatus,
        attendanceTime: LocalDateTime? = this.attendanceTime
    ): Attendance {
        return this.copy(
            attendanceId = attendanceId,
            generation = generation,
            week = week,
            member = member,
            attendanceStatus = attendanceStatus,
            attendanceTime = attendanceTime
        )
    }

    companion object {
        fun newAttendance(
            generation: Int,
            week: Int,
            member: Member,
            attendance: AttendanceStatus = AttendanceStatus.ATTENDANCE_ON_HOLD
        ) = Attendance(
            attendanceId = generateULID(),
            generation = generation,
            week = week,
            member = member,
            attendanceStatus = attendance,
            attendanceTime = null
        )
    }
}
