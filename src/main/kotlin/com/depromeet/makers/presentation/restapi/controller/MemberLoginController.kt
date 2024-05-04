package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.GenerateTokenWithEmailAndPassCord
import com.depromeet.makers.infrastructure.token.JWTTokenProvider
import com.depromeet.makers.presentation.restapi.dto.request.MemberLoginRequest
import com.depromeet.makers.presentation.restapi.dto.response.MemberLoginResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth/login")
class MemberLoginController(
    private val jwtTokenProvider: JWTTokenProvider,
    private val generateTokenWithEmailAndPassCord: GenerateTokenWithEmailAndPassCord,
) {
    @Operation(summary = "이메일 & 코드로 로그인", description = "이메일과 패스키로 토큰을 발급합니다")
    @PostMapping
    fun loginWithEmailAndPassCord(
        @RequestBody @Valid request: MemberLoginRequest,
    ): MemberLoginResponse {
        val tokens = generateTokenWithEmailAndPassCord.execute(
            GenerateTokenWithEmailAndPassCord.GenerateTokenWithEmailAndPassCordInput(
                email = request.email,
                passCord = request.passCord,
            )
        )
        return MemberLoginResponse(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
        )
    }

    @GetMapping
    fun test(
        @RequestParam key: String
    ) {
        println(jwtTokenProvider.parseAuthentication(key).authorities)
    }
}
