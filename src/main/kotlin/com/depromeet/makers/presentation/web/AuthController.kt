package com.depromeet.makers.presentation.web

import com.depromeet.makers.presentation.web.dto.request.AppleLoginRequest
import com.depromeet.makers.presentation.web.dto.request.KakaoLoginRequest
import com.depromeet.makers.presentation.web.dto.request.RefreshTokenRequest
import com.depromeet.makers.presentation.web.dto.response.AuthenticationResponse
import com.depromeet.makers.service.MemberService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val memberService: MemberService,
) {
    @PostMapping("/v1/auth/kakao")
    fun kakaoLogin(
        @RequestBody kakaoLoginRequest: KakaoLoginRequest,
    ): AuthenticationResponse {
        val loginResult = memberService.kakaoLogin(kakaoLoginRequest.accessToken)
        return AuthenticationResponse.from(loginResult)
    }

    @PostMapping("/v1/auth/apple")
    fun appleLogin(
        @RequestBody appleLoginRequest: AppleLoginRequest,
    ): AuthenticationResponse {
        val loginResult = memberService.appleLogin(appleLoginRequest.identityToken)
        return AuthenticationResponse.from(loginResult)
    }

    @PostMapping("/v1/auth/test")
    fun testLogin(): AuthenticationResponse {
        val loginResult = memberService.testLogin()
        return AuthenticationResponse.from(loginResult)
    }

    @PostMapping("/v1/auth/refresh")
    fun refresh(
        @RequestBody refreshTokenRequest: RefreshTokenRequest,
    ): AuthenticationResponse {
        val loginResult = memberService.refreshWithRefreshToken(refreshTokenRequest.refreshToken)
        return AuthenticationResponse.from(loginResult)
    }
}
