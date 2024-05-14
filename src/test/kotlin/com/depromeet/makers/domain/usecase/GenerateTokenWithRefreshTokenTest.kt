package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.PassCordNotSetException
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.TokenGateway
import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.util.EncryptUtils
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.mockk

class GenerateTokenWithRefreshTokenTest: BehaviorSpec({
    Given("패스코드가 지정되지 않는 사용자일때") {
        val memberGateway = mockk<MemberGateway>()
        val tokenGateway = mockk<TokenGateway>()
        val generateTokenWithRefreshToken = GenerateTokenWithRefreshToken(
            memberGateway, tokenGateway
        )
        val mockMember = Member(
            memberId = "mockMemberId",
            name = "mockName",
            email = "email",
            passCord = null,
            generations = emptySet()
        )
        every { tokenGateway.extractMemberIdFromRefreshToken(any()) } returns "sampleId"
        every { memberGateway.getById(any()) } returns mockMember
        When("execute를 실행하면") {
            val executor = {
                generateTokenWithRefreshToken.execute(
                    GenerateTokenWithRefreshToken.GenerateTokenWithRefreshTokenInput(
                        refreshToken = "example"
                    )
                )
            }
            Then("PassCordNotSetException을 던짐") {
                shouldThrow<PassCordNotSetException>(executor)
            }
        }
    }

    Given("비밀번호가 존재하는 사용자일 때") {
        val memberGateway = mockk<MemberGateway>()
        val tokenGateway = mockk<TokenGateway>()
        val generateTokenWithRefreshToken = GenerateTokenWithRefreshToken(
            memberGateway, tokenGateway
        )
        val mockMember = Member(
            memberId = "mockMemberId",
            name = "mockName",
            email = "email",
            passCord = "",
            generations = emptySet()
        )
        every { tokenGateway.extractMemberIdFromRefreshToken(any()) } returns "sampleId"
        every { tokenGateway.generateRefreshToken(any()) } returns "sampleToken"
        every { tokenGateway.generateAccessToken(any()) } returns "sampleToken"
        every { memberGateway.getById(any()) } returns mockMember
        When("execute를 실행하면") {
            generateTokenWithRefreshToken.execute(
                GenerateTokenWithRefreshToken.GenerateTokenWithRefreshTokenInput(
                    refreshToken = "example"
                )
            )
            Then("아무일도 일어나지 않음 (정상)")
        }
    }
})
