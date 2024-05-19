package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.SessionNotFoundException
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.domain.model.Session

class GetAttendancesByWeek(
    private val attendanceGateway: AttendanceGateway,
    private val sessionGateway: SessionGateway,
) : UseCase<GetAttendancesByWeek.GetAttendancesByWeekInput, Pair<List<Attendance>, Session>> {
    data class GetAttendancesByWeekInput(
        val generation: Int,
        val week: Int,
    )

    override fun execute(input: GetAttendancesByWeekInput): Pair<List<Attendance>, Session> {
        return Pair(
            attendanceGateway.findAllByGenerationAndWeek(input.generation, input.week),
            sessionGateway.findByGenerationAndWeek(input.generation, input.week) ?: throw SessionNotFoundException()
        )
    }
}
