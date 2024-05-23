package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.usecase.GetAttendancesByWeek.GetAttendancesByWeekOutput
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
    val teams: List<AttendanceStatsByTeamResponse>,
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
        fun fromDomain(output: GetAttendancesByWeekOutput): AttendanceStatsResponse {
            return AttendanceStatsResponse(
                generation = output.session.generation,
                week = output.session.week,
                sessionDate = output.session.startTime,
                attendancePercentage = output.attendancePercentage.toInt(),
                attendanceCount = output.totalAttendance,
                memberCount = output.totalMember,
                teams = output.teamAttendances.map { (teamId, teamAttendancesStats) ->
                    AttendanceStatsByTeamResponse.new(
                        teamNumber = teamId,
                        attendanceCount = teamAttendancesStats.attendanceCount,
                        memberCount = teamAttendancesStats.memberCount,
                    )
                }.sortedBy { it.teamNumber }
            )
        }
    }
}
