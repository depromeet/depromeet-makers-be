package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.CheckInSession
import com.depromeet.makers.domain.usecase.CheckInSessionWithCode
import com.depromeet.makers.domain.usecase.GetCheckInStatus
import com.depromeet.makers.presentation.restapi.dto.request.CheckInCodeRequest
import com.depromeet.makers.presentation.restapi.dto.request.GetLocationRequest
import com.depromeet.makers.presentation.restapi.dto.response.AttendanceResponse
import com.depromeet.makers.presentation.restapi.dto.response.CheckInStatusResponse
import com.depromeet.makers.presentation.restapi.dto.response.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Tag(name = "출석 관리 API", description = "출석 관리 API")
@RestController
@RequestMapping("/v1/check-in")
class CheckInController(
    private val checkInSession: CheckInSession,
    private val checkInSessionWithCode: CheckInSessionWithCode,
    private val getCheckInStatus: GetCheckInStatus,
) {
    @Operation(summary = "세션 출석", description = "세션에 출석합니다. (서버에서 현재 시간을 기준으로 출석 처리)")
    @PostMapping
    fun checkInSession(
        authentication: Authentication,
        @RequestBody request: GetLocationRequest,
    ): AttendanceResponse =
        AttendanceResponse.fromDomain(
            checkInSession.execute(
                CheckInSession.CheckInSessionInput(
                    now = LocalDateTime.now(),
                    memberId = authentication.name,
                    latitude = request.latitude,
                    longitude = request.longitude,
                ),
            ),
        )

    @Operation(
        summary = "코드 기반 세션 출석",
        description = "세션에 출석합니다. (출석 코드를 기반으로 출석 처리)",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "출석 성공",
                content = [Content(schema = Schema(implementation = AttendanceResponse::class))],
            ),
            ApiResponse(
                responseCode = "AT0008",
                description = "출석 코드가 불일치",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        examples = [ExampleObject(
                            "{\n" +
                                    "  \"code\": \"AT0008\",\n" +
                                    "  \"message\": \"출석 코드가 일치하지 않습니다.\",\n" +
                                    "  \"data\": {\n" +
                                    "    \"retryCount\": 1\n" +
                                    "  }\n" +
                                    "}"
                        )],
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "AT0010",
                description = "출석 시도 횟수 초과",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class),
                        examples = [ExampleObject(
                            "{\n" +
                                    "  \"code\": \"AT0010\",\n" +
                                    "  \"message\": \"출석 시도 횟수를 초과하였습니다.\"\n" +
                                    "}"
                        )],
                    ),
                ],
            ),
        ]
    )
    @PostMapping("/code")
    fun checkInSessionWithCode(
        authentication: Authentication,
        @RequestBody request: CheckInCodeRequest,
    ): AttendanceResponse =
        AttendanceResponse.fromDomain(
            checkInSessionWithCode.execute(
                CheckInSessionWithCode.CheckInSessionWithCodeInput(
                    memberId = authentication.name,
                    now = LocalDateTime.now(),
                    code = request.code,
                ),
            ),
        )

    @Operation(summary = "세션 출석 상태", description = "현재 세션 출석 상태 확인합니다. (CheckInStatusResponse 스키마에 자세한 설명있습니다.)")
    @GetMapping
    fun checkInStatus(authentication: Authentication): CheckInStatusResponse {
        val checkInStatus =
            getCheckInStatus.execute(
                GetCheckInStatus.GetCheckInStatusInput(
                    now = LocalDateTime.now(),
                    memberId = authentication.name,
                ),
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
