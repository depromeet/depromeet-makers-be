package com.depromeet.makers.presentation.restapi.dto.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "출석 조회 결과 DTO")
data class ViewAttendanceStatsResponse(

    @Schema(description = "출석률", example = "50")
    val attendancePercentage: Int,

    @Schema(description = "출석 인원", example = "20")
    val attendanceCount: Int,

    @Schema(description = "전체 인원", example = "40")
    val memberCount: Int,

    @Schema(description = "출석한 팀 목록")
    val teams: List<TeamAttendanceResponse>,
) {

    data class TeamAttendanceResponse(
        @Schema(description = "팀 번호", example = "1")
        val teamNumber: Int,

        @Schema(description = "팀 출석 인원", example = "4")
        val attendanceCount: Int,

        @Schema(description = "팀원 인원", example = "10")
        val memberCount: Int,
    ) {
    }
}
