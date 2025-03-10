package com.depromeet.makers.components

import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import com.depromeet.makers.presentation.web.dto.response.AppleKeyResponse
import com.depromeet.makers.presentation.web.dto.response.KakaoAccountResponse
import com.depromeet.makers.repository.client.AppleAuthClient
import com.depromeet.makers.repository.client.KakaoAuthClient
import com.depromeet.makers.util.logger
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWK
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class SocialLoginProvider(
    private val objectMapper: ObjectMapper,
    private val appleAuthClient: AppleAuthClient,
    private val kakaoAuthClient: KakaoAuthClient,
) {
    private val logger = logger()

    fun authenticateFromKakao(accessToken: String): KakaoAccountResponse = try {
        kakaoAuthClient.getKakaoAccount(mapOf("Authorization" to "Bearer $accessToken"))
    } catch(e: Exception) {
        logger.error("[SocialLoginProvider] failed kakao with error - ${e.message}")
        throw DomainException(ErrorCode.INTERNAL_ERROR)
    }


    fun authenticateFromApple(identityToken: String): String {
        val pubKeys = try {
            appleAuthClient.getApplyKeys()
        } catch(e: Exception) {
            logger.error("[SocialLoginProvider] failed applekey with error - ${e.message}")
            throw DomainException(ErrorCode.INTERNAL_ERROR)
        }
        return getUserIdFromAppleIdentity(pubKeys.keys.toTypedArray(), identityToken)
    }


    private fun getUserIdFromAppleIdentity(keys: Array<AppleKeyResponse>, identityToken: String): String {
        val tokenParts = identityToken.split("\\.")
        val headerPart = String(Base64.getDecoder().decode(tokenParts[0]))
        val headerNode = objectMapper.readTree(headerPart)
        val kid = headerNode.get("kid").asText()

        val keyStr = keys.firstOrNull { it.kid == kid }
            ?: throw RuntimeException("Apple Key not found")

        val pubKey = JWK.parse(objectMapper.writeValueAsString(keyStr))
            .toRSAKey()
            .toRSAPublicKey()

        return Jwts.parser()
            .verifyWith(pubKey)
            .build()
            .parseSignedClaims(identityToken)
            .payload
            .get("sub", String::class.java)
    }
}
