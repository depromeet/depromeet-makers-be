package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.GetMemberAttendances
import com.depromeet.makers.presentation.restapi.dto.response.AttendanceResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@Tag(name = "출석 관리 API", description = "출석 관리 API")
@RestController
@RequestMapping("/v1/attendances")
class AttendanceController(
    private val getMemberAttendances: GetMemberAttendances,
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
}
