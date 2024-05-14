package com.depromeet.makers.infrastructure.gateway

import com.depromeet.makers.domain.gateway.TokenGateway
import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.infrastructure.token.JWTTokenProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class TokenGatewayImpl(
    private val tokenProvider: JWTTokenProvider,
) : TokenGateway {
    override fun generateAccessToken(member: Member): String {
        val authorities = member.generations.map { SimpleGrantedAuthority(it.role.roleName) }
        val authentication = UsernamePasswordAuthenticationToken(
            member.memberId,
            null,
            authorities,
        )
        return tokenProvider.generateAccessToken(authentication)
    }

    override fun generateRefreshToken(member: Member): String {
        val authentication = UsernamePasswordAuthenticationToken(
            member.memberId,
            null,
        )
        return tokenProvider.generateRefreshToken(authentication)
    }

    override fun extractMemberIdFromRefreshToken(token: String): String {
        return tokenProvider.getMemberIdFromRefreshToken(token)
    }
}
