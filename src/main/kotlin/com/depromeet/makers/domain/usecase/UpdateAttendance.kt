package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.domain.model.AttendanceStatus
import com.depromeet.makers.util.logger

class UpdateAttendance(
    private val attendanceGateway: AttendanceGateway,
) : UseCase<UpdateAttendance.UpdateAttendanceInput, Attendance> {
    private val logger = logger()

    data class UpdateAttendanceInput(
        val adminId: String,
        val attendanceId: String,
        val attendanceStatus: AttendanceStatus,
    )

    override fun execute(input: UpdateAttendanceInput): Attendance {
        val attendance = attendanceGateway.getById(input.attendanceId)

        logger.warn("어드민(${input.adminId})이 ${attendance.member.name}의 출석 상태를 ${attendance.attendanceStatus}에서 ${input.attendanceStatus}로 변경했습니다.")

        return attendanceGateway.save(
            attendance.update(
                attendanceStatus = input.attendanceStatus,
            )
        )
    }
}
