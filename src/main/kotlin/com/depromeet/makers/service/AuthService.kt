package com.depromeet.makers.service

import com.depromeet.makers.components.JWTTokenProvider
import com.depromeet.makers.components.SocialLoginProvider
import com.depromeet.makers.domain.Member
import com.depromeet.makers.domain.MemberVerification
import com.depromeet.makers.domain.MemberVerificationKey
import com.depromeet.makers.domain.SocialCredentials
import com.depromeet.makers.domain.SocialCredentialsKey
import com.depromeet.makers.domain.enums.MemberPosition
import com.depromeet.makers.domain.enums.MemberRole
import com.depromeet.makers.domain.enums.MemberStatus
import com.depromeet.makers.domain.enums.SocialCredentialsProvider
import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import com.depromeet.makers.domain.vo.TokenPair
import com.depromeet.makers.presentation.web.dto.request.RegisterRequest
import com.depromeet.makers.presentation.web.dto.request.RegisterWithVerifyRequest
import com.depromeet.makers.repository.MemberRepository
import com.depromeet.makers.repository.MemberVerificationRepository
import com.depromeet.makers.repository.SocialCredentialsRepository
import com.depromeet.makers.util.StringUtil
import org.bson.types.ObjectId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtTokenProvider: JWTTokenProvider,
    private val socialLoginProvider: SocialLoginProvider,
    private val memberRepository: MemberRepository,
    private val memberVerificationRepository: MemberVerificationRepository,
    private val socialCredentialsRepository: SocialCredentialsRepository,
) {
    fun testLogin(id: String): TokenPair {
        val socialCredentialsKey = SocialCredentialsKey(
            provider = SocialCredentialsProvider.TEST,
            externalIdentifier = id,
        )
        return loginWithCredential(
            socialCredentialsKey = socialCredentialsKey,
            email = null,
        )
    }

    fun kakaoLogin(accessToken: String): TokenPair {
        val kakaoLoginResponse = socialLoginProvider.authenticateFromKakao(accessToken)
        val socialCredentialsKey = SocialCredentialsKey(
            provider = SocialCredentialsProvider.KAKAO,
            externalIdentifier = kakaoLoginResponse.id.toString(),
        )
        return loginWithCredential(
            socialCredentialsKey = socialCredentialsKey,
            email = kakaoLoginResponse.kakaoAccount.email,
        )
    }

    fun appleLogin(identityToken: String): TokenPair {
        val appleId = socialLoginProvider.authenticateFromApple(identityToken)
        val socialCredentialsKey = SocialCredentialsKey(
            provider = SocialCredentialsProvider.APPLE,
            externalIdentifier = appleId,
        )
        return loginWithCredential(
            socialCredentialsKey = socialCredentialsKey,
            email = null,
        )
    }

    fun register(request: RegisterRequest) {
        val previousMember = memberRepository.findFirstByEmail(request.email)
        if (previousMember != null) {
            // 이미 같은 이메일의 사용자가 존재하는 경우 에러
            throw DomainException(ErrorCode.BAD_REQUEST)
        }

        val socialCredentialsKey = when {
            request.appleLogin != null -> {
                val appleId = socialLoginProvider.authenticateFromApple(request.appleLogin.identityToken)
                SocialCredentialsKey(
                    provider = SocialCredentialsProvider.APPLE,
                    externalIdentifier = appleId,
                )
            }
            request.kakaoLogin != null -> {
                val kakaoLoginResponse = socialLoginProvider.authenticateFromKakao(request.kakaoLogin.accessToken)
                SocialCredentialsKey(
                    provider = SocialCredentialsProvider.KAKAO,
                    externalIdentifier = kakaoLoginResponse.id.toString(),
                )
            }
            else -> throw RuntimeException("Cannot be reached")
        }

        val newMember = Member.create(
            name = request.name,
            email = request.email,
            deviceToken = null,
            position = request.position,
            role = MemberRole.USER,
            status = MemberStatus.PENDING,
            teamHistory = emptyList(),
        )

        // 해당 소셜 계정에 대해 계정 매핑
        memberRepository.save(newMember).also {
            socialCredentialsRepository.save(SocialCredentials(socialCredentialsKey, newMember))
        }
    }

    fun registerWithEmailVerify(request: RegisterWithVerifyRequest): TokenPair {
        val verification = memberVerificationRepository.findByIdOrNull(
            MemberVerificationKey(ObjectId(request.memberId), request.code)
        ) ?: throw DomainException(ErrorCode.NOT_FOUND)

        if (verification.isExpired()) {
            throw DomainException(ErrorCode.VERIFICATION_EXPIRED)
        }

        val member = memberRepository.findByIdOrNull(verification.key.memberId)
            ?: throw DomainException(ErrorCode.NOT_FOUND)

        val socialCredentialsKey = when {
            request.appleLogin != null -> {
                val appleId = socialLoginProvider.authenticateFromApple(request.appleLogin.identityToken)
                SocialCredentialsKey(
                    provider = SocialCredentialsProvider.APPLE,
                    externalIdentifier = appleId,
                )
            }
            request.kakaoLogin != null -> {
                val kakaoLoginResponse = socialLoginProvider.authenticateFromKakao(request.kakaoLogin.accessToken)
                SocialCredentialsKey(
                    provider = SocialCredentialsProvider.KAKAO,
                    externalIdentifier = kakaoLoginResponse.id.toString(),
                )
            }
            else -> throw RuntimeException("Cannot be reached")
        }

        // 기존 사용자 소셜 매핑에 계정 추가 매핑
        socialCredentialsRepository.save(SocialCredentials(socialCredentialsKey, member))
        return generateTokenFromMember(member)
    }

    fun requestEmailVerification(memberId: String) {
        val member = memberRepository.findByIdOrNull(ObjectId(memberId)) ?: throw DomainException(ErrorCode.NOT_FOUND)

        val randomString = StringUtil.generateRandomString(6)
        memberVerificationRepository.save(MemberVerification.create(ObjectId(memberId), randomString))
        // TODO: 이메일 발송
        // sendEmail(member.email)
    }

    fun refreshWithRefreshToken(refreshToken: String): TokenPair {
        val memberId = jwtTokenProvider.getMemberIdFromRefreshToken(refreshToken)
        val member = memberRepository.findByIdOrNull(memberId) ?: throw DomainException(ErrorCode.NOT_FOUND)
        return generateTokenFromMember(member)
    }

    private fun loginWithCredential(socialCredentialsKey: SocialCredentialsKey, email: String?): TokenPair {
        // 유저 정보 확인
        val retrievedMember = socialCredentialsRepository.findByIdOrNull(socialCredentialsKey)?.member ?: run {
            // 이메일 있으면 가입 처리, 그외 미가입 유저 (NOT_FOUND)
            if (email == null) {
                throw DomainException(ErrorCode.NOT_FOUND)
            }

            val previousMember = memberRepository.findFirstByEmail(email) ?: throw DomainException(ErrorCode.NOT_FOUND)

            // 기가입자일 경우 소셜 계정 링크
            socialCredentialsRepository.save(SocialCredentials(socialCredentialsKey, previousMember))
            return@run previousMember
        }

        // 가입 대기인 경우 에러
        if (retrievedMember.isPendingStatus()) {
            throw DomainException(ErrorCode.FORBIDDEN)
        }

        return generateTokenFromMember(retrievedMember)
    }

    private fun generateTokenFromMember(member: Member): TokenPair {
        val authorities = listOf(SimpleGrantedAuthority(member.role.name))
        val authentication = UsernamePasswordAuthenticationToken(member.id, null, authorities)
        return jwtTokenProvider.generateTokenPair(authentication)
    }
}
