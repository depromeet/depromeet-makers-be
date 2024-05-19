package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.SessionNotFoundException
import com.depromeet.makers.domain.gateway.AttendanceGateway
import com.depromeet.makers.domain.gateway.SessionGateway
import com.depromeet.makers.domain.model.AttendanceStatus
import com.depromeet.makers.domain.model.MemberRole
import com.depromeet.makers.domain.model.Session
import com.depromeet.makers.domain.usecase.GetAttendancesByWeek.GetAttendancesByWeekOutput
import com.depromeet.makers.domain.usecase.GetAttendancesByWeek.GetAttendancesByWeekOutput.TeamAttendancesStats

class GetAttendancesByWeek(
    private val attendanceGateway: AttendanceGateway,
    private val sessionGateway: SessionGateway,
) : UseCase<GetAttendancesByWeek.GetAttendancesByWeekInput, GetAttendancesByWeekOutput> {
    data class GetAttendancesByWeekInput(
        val generation: Int,
        val week: Int,
    )

    data class GetAttendancesByWeekOutput(
        val session: Session,
        val attendancePercentage: Double,
        val totalAttendance: Int,
        val totalMember: Int,
        val teamAttendances: Map<Int, TeamAttendancesStats>,
    ) {
        data class TeamAttendancesStats(
            val teamNumber: Int,
            var attendanceCount: Int,
            var memberCount: Int,
        )
    }


    override fun execute(input: GetAttendancesByWeekInput): GetAttendancesByWeekOutput {
        val attendances = attendanceGateway.findAllByGenerationAndWeek(input.generation, input.week)
        val session = sessionGateway.findByGenerationAndWeek(input.generation, input.week) ?: throw SessionNotFoundException()

        var totalAttendanceCount = 0
        var totalMemberCount = 0
        val teams = mutableMapOf<Int, TeamAttendancesStats>()

        attendances
            .map { Pair(it.member.generations.find { generation -> generation.generationId == it.generation }, it) }
            .filter { (memberGeneration, _) ->
                // 현재 기수의 ROLE이 MEMBER인 경우만 집계
                memberGeneration?.role == MemberRole.MEMBER
            }
            .filter { (_, attendance) ->
                // 출석의 대상이 되는 인원들만 집계
                attendance.attendanceStatus == AttendanceStatus.ATTENDANCE || attendance.attendanceStatus == AttendanceStatus.ATTENDANCE_ON_HOLD
            }
            .forEach { (memberGeneration, attendance) ->
                val teamId = memberGeneration!!.groupId!!
                val team = teams.getOrDefault(teamId, TeamAttendancesStats(teamId, 0, 0))
                when (attendance.attendanceStatus) {
                    AttendanceStatus.ATTENDANCE -> {
                        team.attendanceCount++
                        team.memberCount++
                        totalAttendanceCount++
                        totalMemberCount++
                    }

                    AttendanceStatus.ATTENDANCE_ON_HOLD -> {
                        team.memberCount++
                        totalMemberCount++
                    }

                    else -> {
                        // do nothing
                    }
                }
                teams[teamId] = team
            }
        return GetAttendancesByWeekOutput(
            session = session,
            attendancePercentage = if (totalMemberCount == 0) 0.0 else (totalAttendanceCount.toDouble() / totalMemberCount.toDouble() * 100),
            totalAttendance = totalAttendanceCount,
            totalMember = totalMemberCount,
            teamAttendances = teams,
        )
    }
}
