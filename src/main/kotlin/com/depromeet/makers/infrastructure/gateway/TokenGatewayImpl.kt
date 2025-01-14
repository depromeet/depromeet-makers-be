package com.depromeet.makers.infrastructure.gateway

import com.depromeet.makers.domain.gateway.TokenGateway
import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.infrastructure.token.JWTTokenProvider
import com.depromeet.makers.properties.DepromeetProperties
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

@Component
class TokenGatewayImpl(
    private val tokenProvider: JWTTokenProvider,
    private val depromeetProperties: DepromeetProperties,
) : TokenGateway {
    override fun generateAccessToken(member: Member): String {
        val role = member.currentRole(depromeetProperties.generation)
        val authentication = UsernamePasswordAuthenticationToken(
            member.memberId,
            null,
            listOf(SimpleGrantedAuthority(role.roleName)),
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
