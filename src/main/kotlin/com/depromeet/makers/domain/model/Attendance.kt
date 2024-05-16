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
        // 출석 가능 시간은 세션 시작 시간의 30분 전부터 120분 후까지 입니다. (정책에 따라 수정 필요)
        if (this.isBefore(sessionStartTime.minusMinutes(30))) {
            throw AttendanceBeforeTimeException()
        }
        if (this.isAfter(sessionStartTime.plusMinutes(120))) {
            throw AttendanceAfterTimeException()
        }
        return true
    }

    private fun LocalDateTime.checkIn(sessionStartTime: LocalDateTime): AttendanceStatus {
        // 출석 인정 시간은 세션 시작 시간의 30분 전부터 30분 후까지 입니다. (정책에 따라 수정 필요)
        if (this.isAfter(sessionStartTime.minusMinutes(30)) && this.isBefore(sessionStartTime.plusMinutes(30))) {
            return AttendanceStatus.ATTENDANCE
        }
        // 지각 인정 시간은 세션 시작 시간의 30분 후부터 120분 후까지 입니다. (정책에 따라 수정 필요)
        else if (this.isAfter(sessionStartTime.plusMinutes(30)) && this.isBefore(sessionStartTime.plusMinutes(120))) {
            return AttendanceStatus.TARDY
        }
        throw AttendanceAfterTimeException()
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
