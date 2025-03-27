package com.depromeet.makers.presentation.web

import com.depromeet.makers.presentation.web.dto.request.AppleLoginRequest
import com.depromeet.makers.presentation.web.dto.request.KakaoLoginRequest
import com.depromeet.makers.presentation.web.dto.request.RefreshTokenRequest
import com.depromeet.makers.presentation.web.dto.request.TestLoginRequest
import com.depromeet.makers.presentation.web.dto.response.AuthenticationResponse
import com.depromeet.makers.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인증 API", description = "로그인 및 토큰 관련 API")
@RestController
class AuthController(
    private val authService: AuthService,
) {
    @Operation(summary = "카카오 로그인", description = "카카오 엑세스 토큰으로 로그인을 수행합니다.")
    @PostMapping("/v1/auth/kakao")
    fun kakaoLogin(
        @RequestBody kakaoLoginRequest: KakaoLoginRequest,
    ): AuthenticationResponse {
        val loginResult = authService.kakaoLogin(kakaoLoginRequest.accessToken)
        return AuthenticationResponse.from(loginResult)
    }

    @Operation(summary = "애플 로그인", description = "애플 ID 토큰으로 로그인을 수행합니다.")
    @PostMapping("/v1/auth/apple")
    fun appleLogin(
        @RequestBody appleLoginRequest: AppleLoginRequest,
    ): AuthenticationResponse {
        val loginResult = authService.appleLogin(appleLoginRequest.identityToken)
        return AuthenticationResponse.from(loginResult)
    }

    @Operation(summary = "테스트 로그인", description = "테스트용 로그인을 수행합니다.")
    @PostMapping("/v1/auth/test")
    fun testLogin(
        @RequestBody testLoginRequest: TestLoginRequest,
    ): AuthenticationResponse {
        val loginResult = authService.testLogin(testLoginRequest.externalIdentifier)
        return AuthenticationResponse.from(loginResult)
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 엑세스 토큰을 갱신합니다.")
    @PostMapping("/v1/auth/refresh")
    fun refresh(
        @RequestBody refreshTokenRequest: RefreshTokenRequest,
    ): AuthenticationResponse {
        val loginResult = authService.refreshWithRefreshToken(refreshTokenRequest.refreshToken)
        return AuthenticationResponse.from(loginResult)
    }
}
