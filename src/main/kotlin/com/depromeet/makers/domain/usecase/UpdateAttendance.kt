package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.domain.model.AttendanceStatus

class UpdateAttendance(
    private val attendanceGateway: AttendanceGateway,
) : UseCase<UpdateAttendance.UpdateAttendanceInput, Attendance> {
    data class UpdateAttendanceInput(
        val attendanceId: String,
        val attendanceStatus: AttendanceStatus,
    )

    override fun execute(input: UpdateAttendanceInput): Attendance {
        val attendance = attendanceGateway.getById(input.attendanceId)

        return attendanceGateway.save(
            attendance.update(
                attendanceStatus = input.attendanceStatus,
            )
        )
    }
}
