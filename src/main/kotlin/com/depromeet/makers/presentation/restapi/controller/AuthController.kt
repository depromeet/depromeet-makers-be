package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.usecase.GenerateTokenWithEmailAndPassCord
import com.depromeet.makers.domain.usecase.GenerateTokenWithRefreshToken
import com.depromeet.makers.domain.usecase.UpdateDefaultMemberPassCord
import com.depromeet.makers.presentation.restapi.dto.request.MemberDefaultPassCordRequest
import com.depromeet.makers.presentation.restapi.dto.request.MemberLoginRequest
import com.depromeet.makers.presentation.restapi.dto.request.MemberRefreshTokenRequest
import com.depromeet.makers.presentation.restapi.dto.response.MemberLoginResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인증 관련 API", description = "인증 (로그인, 패스워드 등) 관리 API")
@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val generateTokenWithEmailAndPassCord: GenerateTokenWithEmailAndPassCord,
    private val generateTokenWithRefreshToken: GenerateTokenWithRefreshToken,
    private val updateDefaultMemberPassCord: UpdateDefaultMemberPassCord,
) {
    @Operation(summary = "이메일 & 코드로 로그인", description = "이메일과 패스키로 토큰을 발급합니다")
    @PostMapping("/login")
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

    @Operation(summary = "토큰 갱신 요청", description = "리프레시 토큰으로 기존 토큰을 갱신합니다")
    @PostMapping("/refresh")
    fun loginWithRefreshToken(
        @RequestBody @Valid request: MemberRefreshTokenRequest,
    ): MemberLoginResponse {
        val tokens = generateTokenWithRefreshToken.execute(
            GenerateTokenWithRefreshToken.GenerateTokenWithRefreshTokenInput(
                refreshToken = request.refreshToken,
            )
        )
        return MemberLoginResponse(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
        )
    }

    @Operation(summary = "초기 비밀번호 설정", description = "초기 비밀번호를 설정합니다. (계정별 단 1회만 가능)")
    @PostMapping("/default-passcord")
    fun setDefaultPassCord(
        @RequestBody @Valid request: MemberDefaultPassCordRequest
    ) {
        updateDefaultMemberPassCord.execute(
            UpdateDefaultMemberPassCord.UpdateDefaultMemberPassCordInput(
                email = request.email,
                passCord = request.passCord
            )
        )
    }
}
