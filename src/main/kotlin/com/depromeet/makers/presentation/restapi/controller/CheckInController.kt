package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.CheckInSession
import com.depromeet.makers.domain.usecase.GetCheckInStatus
import com.depromeet.makers.presentation.restapi.dto.request.GetLocationRequest
import com.depromeet.makers.presentation.restapi.dto.response.AttendanceResponse
import com.depromeet.makers.presentation.restapi.dto.response.CheckInStatusResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "출석 관리 API", description = "출석 관리 API")
@RestController
@RequestMapping("/v1/check-in")
class CheckInController(
    private val checkInSession: CheckInSession,
    private val getCheckInStatus: GetCheckInStatus,
) {

    @Operation(summary = "세션 출석", description = "세션에 출석합니다. (서버에서 현재 시간을 기준으로 출석 처리)")
    @PostMapping
    fun checkInSession(
        authentication: Authentication,
        @RequestBody request: GetLocationRequest,
    ): AttendanceResponse {
        return AttendanceResponse.fromDomain(
            checkInSession.execute(
                CheckInSession.CheckInSessionInput(
                    now = LocalDateTime.now(),
                    memberId = authentication.name,
                    latitude = request.latitude,
                    longitude = request.longitude,
                )
            )
        )
    }

    @Operation(summary = "세션 출석 상태", description = "현재 세션 출석 상태 확인합니다. (CheckInStatusResponse 스키마에 자세한 설명있습니다.)")
    @GetMapping
    fun checkInStatus(
        authentication: Authentication,
    ): CheckInStatusResponse {
        val checkInStatus = getCheckInStatus.execute(
            GetCheckInStatus.GetCheckInStatusInput(
                now = LocalDateTime.now(),
                memberId = authentication.name,
            )
        )
        return CheckInStatusResponse(
            generation = checkInStatus.generation,
            week = checkInStatus.week,
            isBeforeSession15minutes = checkInStatus.isBeforeSession15minutes,
            needFloatingButton = checkInStatus.needFloatingButton,
            expectAttendanceStatus = checkInStatus.expectAttendanceStatus,
        )
    }
}
