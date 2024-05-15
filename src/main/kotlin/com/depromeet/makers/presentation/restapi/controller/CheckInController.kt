package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.domain.usecase.CheckInSession
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "출석 관리 API", description = "출석 관리 API")
@RestController
@RequestMapping("/v1/check-in")
class CheckInController(
    private val checkInSession: CheckInSession,
) {

    @PreAuthorize("hasAnyRole('MEMBER', 'ORGANIZER')")
    @Operation(summary = "세션 출석", description = "세션에 출석합니다. (서버에서 현재 시간을 기준으로 출석 처리)")
    @PostMapping
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "출석 성공"),
        ApiResponse(responseCode = "AT0001", description = "출석 시간 이전입니다"),
        ApiResponse(responseCode = "AT0002", description = "출석 시간이 지났습니다"),
        ApiResponse(responseCode = "AT0003", description = "이미 출석했습니다"),
        ApiResponse(responseCode = "AT0004", description = "현재 주차에 해당하는 세션을 찾을 수 없습니다."),
        ApiResponse(responseCode = "AT0005", description = "현재 위치 파라미터가 누락되었습니다."),
        ApiResponse(responseCode = "AT0006", description = "현재 위치와 세션 장소의 거리가 너무 멉니다."),
        )
    fun checkInSession(
        authentication: Authentication,
        @RequestParam("latitude", required = false) latitude: Double?,
        @RequestParam("longitude", required = false) longitude: Double?,
    ) {
        checkInSession.execute(
            CheckInSession.CheckInSessionInput(
                now = LocalDateTime.now(),
                member = authentication.principal as Member,
                latitude = latitude,
                longitude = longitude,
            )
        )
    }
}
