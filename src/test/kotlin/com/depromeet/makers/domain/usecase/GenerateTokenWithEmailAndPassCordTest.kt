package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.MemberNotFoundException
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

class GenerateTokenWithEmailAndPassCordTest: BehaviorSpec({
    Given("존재하지 않는 이메일이 입력되었을 때") {
        val memberGateway = mockk<MemberGateway>()
        val tokenGateway = mockk<TokenGateway>()
        val generateTokenWithEmailAndPassCord = GenerateTokenWithEmailAndPassCord(
            memberGateway, tokenGateway
        )
        every { memberGateway.findByEmail(any()) } returns null
        When("execute를 실행하면") {
            val executor = {
                generateTokenWithEmailAndPassCord.execute(
                    GenerateTokenWithEmailAndPassCord.GenerateTokenWithEmailAndPassCordInput(
                        email = "notexist@naver.com",
                        passCord = ""
                    )
                )
            }
            Then("MemberNotFoundException을 던짐") {
                shouldThrow<MemberNotFoundException>(executor)
            }
        }
    }

    Given("패스코드가 지정되지 않는 사용자일때") {
        val memberGateway = mockk<MemberGateway>()
        val tokenGateway = mockk<TokenGateway>()
        val generateTokenWithEmailAndPassCord = GenerateTokenWithEmailAndPassCord(
            memberGateway, tokenGateway
        )
        val mockMember = Member(
            memberId = "mockMemberId",
            name = "mockName",
            email = "email",
            passCord = null,
            generations = emptySet()
        )
        every { memberGateway.findByEmail(any()) } returns mockMember
        When("execute를 실행하면") {
            val executor = {
                generateTokenWithEmailAndPassCord.execute(
                    GenerateTokenWithEmailAndPassCord.GenerateTokenWithEmailAndPassCordInput(
                        email = "notexist@naver.com",
                        passCord = ""
                    )
                )
            }
            Then("PassCordNotSetException을 던짐") {
                shouldThrow<PassCordNotSetException>(executor)
            }
        }
    }

    Given("입력된 비밀번호가 일치하지 않을 때") {
        val memberGateway = mockk<MemberGateway>()
        val tokenGateway = mockk<TokenGateway>()
        val generateTokenWithEmailAndPassCord = GenerateTokenWithEmailAndPassCord(
            memberGateway, tokenGateway
        )
        val passCord = "000000"
        val mockMember = Member(
            memberId = "mockMemberId",
            name = "mockName",
            email = "email",
            passCord = EncryptUtils.encrypt(passCord),
            generations = emptySet()
        )
        every { memberGateway.findByEmail(any()) } returns mockMember
        When("execute를 실행하면") {
            val executor = {
                generateTokenWithEmailAndPassCord.execute(
                    GenerateTokenWithEmailAndPassCord.GenerateTokenWithEmailAndPassCordInput(
                        email = "notexist@naver.com",
                        passCord = "111111"
                    )
                )
            }
            Then("MemberNotFoundException을 던짐") {
                shouldThrow<MemberNotFoundException>(executor)
            }
        }
    }

    Given("입력된 비밀번호가 일치할 떄") {
        val memberGateway = mockk<MemberGateway>()
        val tokenGateway = mockk<TokenGateway>()
        val generateTokenWithEmailAndPassCord = GenerateTokenWithEmailAndPassCord(
            memberGateway, tokenGateway
        )
        val passCord = "000000"
        val mockMember = Member(
            memberId = "mockMemberId",
            name = "mockName",
            email = "email",
            passCord = EncryptUtils.encrypt(passCord),
            generations = emptySet()
        )
        val temporaryToken = "temptoken123"

        every { memberGateway.findByEmail(any()) } returns mockMember
        every { tokenGateway.generateAccessToken(any()) } returns temporaryToken
        every { tokenGateway.generateRefreshToken(any()) } returns temporaryToken

        When("execute를 실행하면") {
            val result = generateTokenWithEmailAndPassCord.execute(
                GenerateTokenWithEmailAndPassCord.GenerateTokenWithEmailAndPassCordInput(
                    email = "notexist@naver.com",
                    passCord = "000000"
                )
            )
            Then("토큰을 발행함") {
                result.accessToken shouldBeEqual temporaryToken
                result.refreshToken shouldBeEqual temporaryToken
            }
        }
    }
})
