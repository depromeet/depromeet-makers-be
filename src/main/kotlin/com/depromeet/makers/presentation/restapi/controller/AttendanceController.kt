package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.GetMemberAttendances
import com.depromeet.makers.domain.usecase.UpdateAttendance
import com.depromeet.makers.presentation.restapi.dto.request.UpdateAttendanceRequest
import com.depromeet.makers.presentation.restapi.dto.response.AttendanceResponse
import com.depromeet.makers.presentation.restapi.dto.response.UpdateAttendanceResponse
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
) {

    @Operation(summary = "나의 출석 현황 조회", description = "로그인한 사용자의 출석 현황을 조회합니다.")
    @GetMapping("/me")
    fun getMyAttendance(
        authentication: Authentication,
        @RequestParam(defaultValue = "15") generation: Int,
    ): List<AttendanceResponse> {
        val attendances = getMemberAttendances.execute(
            GetMemberAttendances.GetMemberAttendancesInput(
                memberId = authentication.name,
                generation = generation,
            )
        )
        return attendances.map { AttendanceResponse.fromDomain(it) }
    }

    @PreAuthorize("hasRole('ORGANIZER')")
    @Operation(summary = "출석 정보 수정", description = "유저의 출석 정보를 수정합니다.")
    @PutMapping("{attendanceId}")
    fun updateAttendance(
        authentication: Authentication,
        @RequestBody @Valid request: UpdateAttendanceRequest, @PathVariable attendanceId: String,
    ): UpdateAttendanceResponse {
        val attendances = updateAttendance.execute(
            UpdateAttendance.UpdateAttendanceInput(
                authentication.name,
                request.attendanceStatus,
            )
        )
        return UpdateAttendanceResponse.fromDomain(attendances)
    }
}
