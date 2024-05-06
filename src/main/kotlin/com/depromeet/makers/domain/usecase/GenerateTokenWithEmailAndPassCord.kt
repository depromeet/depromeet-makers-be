package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.MemberNotFoundException
import com.depromeet.makers.domain.exception.PassCordNotSetException
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.TokenGateway
import com.depromeet.makers.util.EncryptUtils

class GenerateTokenWithEmailAndPassCord(
    private val memberGateway: MemberGateway,
    private val tokenGateway: TokenGateway,
) : UseCase<GenerateTokenWithEmailAndPassCord.GenerateTokenWithEmailAndPassCordInput,
        GenerateTokenWithEmailAndPassCord.GenerateTokenWithEmailAndPassCordOutput> {
    data class GenerateTokenWithEmailAndPassCordInput(
        val email: String,
        val passCord: String,
    )

    data class GenerateTokenWithEmailAndPassCordOutput(
        val accessToken: String,
        val refreshToken: String,
    )

    override fun execute(input: GenerateTokenWithEmailAndPassCordInput): GenerateTokenWithEmailAndPassCordOutput {
        val member = memberGateway.findByEmail(input.email) ?: throw MemberNotFoundException()
        if (!member.hasPassCord()) throw PassCordNotSetException()

        val isPassCordMatched = EncryptUtils.isMatch(input.passCord, member.passCord!!)
        if (!isPassCordMatched) throw MemberNotFoundException()

        return GenerateTokenWithEmailAndPassCordOutput(
            accessToken = tokenGateway.generateAccessToken(member),
            refreshToken = tokenGateway.generateRefreshToken(member),
        )
    }
}
