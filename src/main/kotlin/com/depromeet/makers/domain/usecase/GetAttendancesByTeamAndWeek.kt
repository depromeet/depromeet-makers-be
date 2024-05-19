package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.model.Attendance

class GetAttendancesByTeamAndWeek(
    private val attendanceGateway: AttendanceGateway,
) : UseCase<GetAttendancesByTeamAndWeek.GetAttendancesByTeamAndWeekInput, List<Attendance>> {
    data class GetAttendancesByTeamAndWeekInput(
        val generation: Int,
        val groupId: Int,
        val week: Int,
    )

    override fun execute(input: GetAttendancesByTeamAndWeekInput): List<Attendance> {
        return attendanceGateway.findAllByGenerationAndWeek(
            input.generation,
            input.week,
        ).filter {
            it.member.generations.any {
                it.generationId == input.generation && it.groupId == input.groupId
            }
        }
    }
}
