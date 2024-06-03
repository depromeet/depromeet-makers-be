package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.PassCordNotSetException
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.gateway.TokenGateway

class GenerateTokenWithRefreshToken(
    private val memberGateway: MemberGateway,
    private val tokenGateway: TokenGateway,
) : UseCase<GenerateTokenWithRefreshToken.GenerateTokenWithRefreshTokenInput,
        GenerateTokenWithRefreshToken.GenerateTokenWithEmailAndPassCordOutput> {
    data class GenerateTokenWithRefreshTokenInput(
        val refreshToken: String,
    )

    data class GenerateTokenWithEmailAndPassCordOutput(
        val accessToken: String,
        val refreshToken: String,
        val email: String,
    )

    override fun execute(input: GenerateTokenWithRefreshTokenInput): GenerateTokenWithEmailAndPassCordOutput {
        val memberId = tokenGateway.extractMemberIdFromRefreshToken(input.refreshToken)
        val member = memberGateway.getById(memberId)
        if (!member.hasPassCord()) throw PassCordNotSetException()

        return GenerateTokenWithEmailAndPassCordOutput(
            accessToken = tokenGateway.generateAccessToken(member),
            refreshToken = tokenGateway.generateRefreshToken(member),
            email = member.email,
        )
    }
}
