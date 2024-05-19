package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.model.Attendance
import com.depromeet.makers.domain.model.AttendanceStatus
import com.depromeet.makers.domain.model.MemberRole
import com.depromeet.makers.domain.model.Session
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "출석 조회 결과 DTO")
data class AttendanceStatsResponse(

    @Schema(description = "기수", example = "15")
    val generation: Int,

    @Schema(description = "주차", example = "1")
    val week: Int,

    @Schema(description = "세션 날짜", example = "2021-10-10T10:00:00")
    val sessionDate: LocalDateTime,

    @Schema(description = "전체 출석률", example = "50")
    val attendancePercentage: Int,

    @Schema(description = "출석 인원", example = "20")
    val attendanceCount: Int,

    @Schema(description = "오늘 세션에 출석 할 인원", example = "40")
    val memberCount: Int,

    @Schema(description = "출석한 팀 목록")
    val teams: Map<Int, AttendanceStatsByTeamResponse>,
) {
    data class AttendanceStatsByTeamResponse(
        @Schema(description = "팀 번호", example = "1")
        val teamNumber: Int,

        @Schema(description = "팀 출석 인원", example = "4")
        var attendanceCount: Int,

        @Schema(description = "팀원 인원", example = "10")
        var memberCount: Int,
    ) {
        companion object {
            fun new(teamNumber: Int, attendanceCount: Int, memberCount: Int) = AttendanceStatsByTeamResponse(
                teamNumber = teamNumber,
                attendanceCount = attendanceCount,
                memberCount = memberCount,
            )
        }
    }

    companion object {
        fun fromDomain(attendances: List<Attendance>, session: Session): AttendanceStatsResponse {
            var totalAttendanceCount = 0
            var totalMemberCount = 0
            val teams = mutableMapOf<Int, AttendanceStatsByTeamResponse>()

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
                    val team = teams.getOrDefault(teamId, AttendanceStatsByTeamResponse.new(teamId, 0, 0))
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
            return AttendanceStatsResponse(
                generation = session.generation,
                week = session.week,
                sessionDate = session.startTime,
                attendancePercentage = if (totalMemberCount == 0) 0 else (totalAttendanceCount.toDouble() / totalMemberCount.toDouble() * 100).toInt(),
                attendanceCount = totalAttendanceCount,
                memberCount = totalMemberCount,
                teams = teams,
            )
        }
    }
}
