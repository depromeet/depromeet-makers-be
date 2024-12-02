package com.depromeet.makers.presentation.restapi.controller

import com.depromeet.makers.domain.exception.MemberNotFoundException
import com.depromeet.makers.domain.usecase.member.GenerateTokenWithEmailAndPassCord
import com.depromeet.makers.domain.usecase.member.GenerateTokenWithRefreshToken
import com.depromeet.makers.domain.usecase.member.GetMemberByEmail
import com.depromeet.makers.domain.usecase.member.UpdateDefaultMemberPassCord
import com.depromeet.makers.presentation.restapi.dto.member.request.MemberDefaultPassCordRequest
import com.depromeet.makers.presentation.restapi.dto.member.request.MemberLoginRequest
import com.depromeet.makers.presentation.restapi.dto.member.request.MemberRefreshTokenRequest
import com.depromeet.makers.presentation.restapi.dto.member.response.CheckMemberExistsByEmailResponse
import com.depromeet.makers.presentation.restapi.dto.member.response.MemberLoginResponse
import com.depromeet.makers.presentation.restapi.dto.member.response.MemberResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "인증 관련 API", description = "인증 (로그인, 패스워드 등) 관리 API")
@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val generateTokenWithEmailAndPassCord: GenerateTokenWithEmailAndPassCord,
    private val generateTokenWithRefreshToken: GenerateTokenWithRefreshToken,
    private val updateDefaultMemberPassCord: UpdateDefaultMemberPassCord,
    private val getMemberByEmail: GetMemberByEmail,
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
        val member = getMemberByEmail.execute(
            GetMemberByEmail.GetMemberByEmailInput(
                email = request.email
            )
        )
        return MemberLoginResponse(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            member = MemberResponse.fromDomain(member)
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
        val member = getMemberByEmail.execute(
            GetMemberByEmail.GetMemberByEmailInput(
                email = tokens.email,
            )
        )
        return MemberLoginResponse(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            member = MemberResponse.fromDomain(member)
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

    @Operation(summary = "사용자 존재 유무 확인", description = "해당 이메일을 가진 사용자가 존재하는지 확인합니다.")
    @GetMapping("/has-member", params = ["email"])
    fun checkMemberExistsByEmail(
        @RequestParam("email") email: String,
    ): CheckMemberExistsByEmailResponse {
        val member = try {
            getMemberByEmail.execute(GetMemberByEmail.GetMemberByEmailInput(email = email))
        } catch(e: MemberNotFoundException) {
            null
        }
        return CheckMemberExistsByEmailResponse(
            isMemberExists = member != null,
            isPassCordAssigned = member?.hasPassCord() == true,
        )
    }
}
