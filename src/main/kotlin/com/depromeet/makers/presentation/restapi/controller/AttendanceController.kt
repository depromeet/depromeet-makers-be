package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.attendence.GetAttendancesByTeamAndWeek
import com.depromeet.makers.domain.usecase.attendence.GetAttendancesByWeek
import com.depromeet.makers.domain.usecase.attendence.GetMemberAttendances
import com.depromeet.makers.domain.usecase.attendence.UpdateAttendance
import com.depromeet.makers.presentation.restapi.dto.attendence.request.UpdateAttendanceRequest
import com.depromeet.makers.presentation.restapi.dto.attendence.response.AttendanceResponse
import com.depromeet.makers.presentation.restapi.dto.attendence.response.AttendanceStatsResponse
import com.depromeet.makers.presentation.restapi.dto.attendence.response.MyAttendanceResponse
import com.depromeet.makers.presentation.restapi.dto.attendence.response.UpdateAttendanceResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*


@Tag(name = "출석 관리 API", description = "출석 관리 API")
@RestController
@RequestMapping("/v1/attendances")
class AttendanceController(
    private val getMemberAttendances: GetMemberAttendances,
    private val updateAttendance: UpdateAttendance,
    private val getAttendancesByTeamAndWeek: GetAttendancesByTeamAndWeek,
    private val getAttendancesByWeek: GetAttendancesByWeek,
) {

    @Operation(summary = "나의 출석 현황 조회", description = "로그인한 사용자의 출석 현황을 조회합니다.")
    @GetMapping("/me")
    fun getMyAttendance(
        authentication: Authentication,
        @RequestParam(defaultValue = "15") generation: Int,
    ): MyAttendanceResponse {
        return MyAttendanceResponse.fromDomain(
            getMemberAttendances.execute(
                GetMemberAttendances.GetMemberAttendancesInput(
                    memberId = authentication.name,
                    generation = generation,
                )
            )
        )
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "출석 정보 수정", description = "유저의 출석 정보를 수정합니다.")
    @PutMapping("{attendanceId}")
    fun updateAttendance(
        @RequestBody @Valid request: UpdateAttendanceRequest, @PathVariable attendanceId: String,
    ): UpdateAttendanceResponse {
        val attendances = updateAttendance.execute(
            UpdateAttendance.UpdateAttendanceInput(
                attendanceId = attendanceId,
                attendanceStatus = request.attendanceStatus,
            )
        )
        return UpdateAttendanceResponse.fromDomain(attendances)
    }

    @Operation(summary = "팀, 주차별 출석률 조회", description = "팀, 주차별 출석률을 조회합니다.")
    @GetMapping("/groups/{groupId}")
    fun getTeamAttendance(
        @RequestParam(defaultValue = "15") generation: Int,
        @RequestParam week: Int,
        @PathVariable groupId: Int,
    ): List<AttendanceResponse> {
        return getAttendancesByTeamAndWeek.execute(
            GetAttendancesByTeamAndWeek.GetAttendancesByTeamAndWeekInput(
                generation = generation,
                groupId = groupId,
                week = week,
            )
        ).map { AttendanceResponse.fromDomain(it) }
            .sortedBy { it.memberName }
    }

    @Operation(summary = "팀 별 전체 출석 통계 조회")
    @GetMapping("/stats")
    fun getAttendanceStats(
        @RequestParam(defaultValue = "15") generation: Int,
        @RequestParam week: Int,
    ): AttendanceStatsResponse {
        return AttendanceStatsResponse.fromDomain(
            getAttendancesByWeek.execute(
                GetAttendancesByWeek.GetAttendancesByWeekInput(
                    generation = generation,
                    week = week,
                )
            )
        )
    }
}
