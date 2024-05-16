package com.depromeet.makers.presentation.restapi.dto.response

import com.depromeet.makers.domain.usecase.GetMemberAttendances
import io.swagger.v3.oas.annotations.media.Schema

data class MyAttendanceResponse(

    @Schema(description = "기수", example = "15")
    val generation: Int,

    @Schema(description = "오프라인 결석 점수", example = "1.0")
    val offlineAbsenceScore: Double,

    @Schema(description = "총 결석 점수 (지각: 0.5)", example = "2.5")
    val totalAbsenceScore: Double,

    val attendances: List<AttendanceResponse>
) {
    companion object {
        fun fromDomain(getMemberAttendancesOutput: GetMemberAttendances.GetMemberAttendancesOutput) = MyAttendanceResponse(
            generation = getMemberAttendancesOutput.generation,
            offlineAbsenceScore = getMemberAttendancesOutput.offlineAbsenceScore,
            totalAbsenceScore = getMemberAttendancesOutput.totalAbsenceScore,
            attendances = getMemberAttendancesOutput.attendances.map { AttendanceResponse.fromDomain(it) }
        )
    }
}
