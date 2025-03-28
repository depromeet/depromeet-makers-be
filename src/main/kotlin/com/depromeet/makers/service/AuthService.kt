package com.depromeet.makers.service

import com.depromeet.makers.components.JWTTokenProvider
import com.depromeet.makers.components.SocialLoginProvider
import com.depromeet.makers.domain.Member
import com.depromeet.makers.domain.SocialCredentials
import com.depromeet.makers.domain.SocialCredentialsKey
import com.depromeet.makers.domain.enums.MemberPosition
import com.depromeet.makers.domain.enums.MemberRole
import com.depromeet.makers.domain.enums.MemberStatus
import com.depromeet.makers.domain.enums.SocialCredentialsProvider
import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import com.depromeet.makers.domain.vo.TokenPair
import com.depromeet.makers.repository.MemberRepository
import com.depromeet.makers.repository.SocialCredentialsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JWTTokenProvider,
    private val socialLoginProvider: SocialLoginProvider,
    private val memberRepository: MemberRepository,
    private val socialCredentialsRepository: SocialCredentialsRepository,
) {
    fun testLogin(id: String): TokenPair {
        val socialCredentialsKey = SocialCredentialsKey(
            provider = SocialCredentialsProvider.TEST,
            externalIdentifier = id,
        )
        return loginWithCredential(socialCredentialsKey)
    }

    fun kakaoLogin(accessToken: String): TokenPair {
        val kakaoLoginResponse = socialLoginProvider.authenticateFromKakao(accessToken)
        val socialCredentialsKey = SocialCredentialsKey(
            provider = SocialCredentialsProvider.KAKAO,
            externalIdentifier = kakaoLoginResponse.id.toString(),
        )
        return loginWithCredential(socialCredentialsKey)
    }

    fun appleLogin(identityToken: String): TokenPair {
        val appleId = socialLoginProvider.authenticateFromApple(identityToken)
        val socialCredentialsKey = SocialCredentialsKey(
            provider = SocialCredentialsProvider.APPLE,
            externalIdentifier = appleId,
        )
        return loginWithCredential(socialCredentialsKey)
    }

    fun refreshWithRefreshToken(refreshToken: String): TokenPair {
        val memberId = jwtTokenProvider.getMemberIdFromRefreshToken(refreshToken)
        val member = memberRepository.findByIdOrNull(memberId) ?: throw DomainException(ErrorCode.NOT_FOUND)
        return generateTokenFromMember(member)
    }

    private fun loginWithCredential(socialCredentialsKey: SocialCredentialsKey): TokenPair {
        val retrievedMember = socialCredentialsRepository.findByIdOrNull(socialCredentialsKey)?.member ?: run {
            val newMember = Member.create(
                name = "test",
                email = "test",
                deviceToken = "test",
                position = MemberPosition.IOS,
                role = MemberRole.ADMIN,
                status = MemberStatus.REGISTERED,
                teamHistory = emptyList(),
            )
            memberRepository.save(newMember).also {
                socialCredentialsRepository.save(SocialCredentials(socialCredentialsKey, it))
            }
        }
        return generateTokenFromMember(retrievedMember)
    }

    private fun generateTokenFromMember(member: Member): TokenPair {
        val authorities = listOf(SimpleGrantedAuthority(member.role.name))
        val authentication = UsernamePasswordAuthenticationToken(member.id, null, authorities)
        return jwtTokenProvider.generateTokenPair(authentication)
    }
}
