package com.depromeet.makers.components

import com.depromeet.makers.config.properties.AppProperties
import com.depromeet.makers.domain.exception.DomainException
import com.depromeet.makers.domain.exception.ErrorCode
import com.depromeet.makers.domain.vo.TokenPair
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JWTTokenProvider(
    private val appProperties: AppProperties,
) {
    private final val signKey: SecretKey = SecretKeySpec(appProperties.token.secretKey.toByteArray(), "AES")
    private val jwtParser = Jwts
        .parser()
        .decryptWith(signKey)
        .build()

    fun generateTokenPair(authentication: Authentication): TokenPair {
        val accessToken = generateAccessToken(authentication)
        val refreshToken = generateRefreshToken(authentication)
        return TokenPair(accessToken, refreshToken)
    }

    private fun generateAccessToken(authentication: Authentication): String {
        val authorities = authentication.authorities.joinToString(",") {
            it.authority
        }
        return Jwts.builder()
            .header()
            .add(TOKEN_TYPE_HEADER_KEY, ACCESS_TOKEN_TYPE_VALUE)
            .and()
            .claims()
            .add(USER_ID_CLAIM_KEY, authentication.name)
            .add(AUTHORITIES_CLAIM_KEY, authorities)
            .and()
            .expiration(generateAccessTokenExpiration())
            .encryptWith(signKey, Jwts.ENC.A128CBC_HS256)
            .compact()
    }

    private fun generateRefreshToken(authentication: Authentication): String = Jwts.builder()
        .header()
        .add(TOKEN_TYPE_HEADER_KEY, REFRESH_TOKEN_TYPE_VALUE)
        .and()
        .claims()
        .add(USER_ID_CLAIM_KEY, authentication.name)
        .and()
        .expiration(generateRefreshTokenExpiration())
        .encryptWith(signKey, Jwts.ENC.A128CBC_HS256)
        .compact()

    fun parseAuthentication(accessToken: String): Authentication {
        val claims = runCatching {
            jwtParser.parseEncryptedClaims(accessToken)
        }.getOrElse {
            throw DomainException(ErrorCode.TOKEN_EXPIRED)
        }
        val tokenType = claims.header[TOKEN_TYPE_HEADER_KEY] ?: throw RuntimeException()
        if (tokenType != ACCESS_TOKEN_TYPE_VALUE) throw RuntimeException()

        val userId = claims.payload[USER_ID_CLAIM_KEY] as? String? ?: throw RuntimeException()
        val authoritiesStr = claims.payload[AUTHORITIES_CLAIM_KEY] as? String?
        val authorities = if (authoritiesStr.isNullOrEmpty()) {
            emptyList()
        } else {
            claims.payload[AUTHORITIES_CLAIM_KEY]?.toString()
                ?.split(",")
                ?.map { SimpleGrantedAuthority("ROLE_$it") }
                ?: emptyList()
        }

        return UsernamePasswordAuthenticationToken(
            userId,
            accessToken,
            authorities,
        )
    }

    fun getMemberIdFromRefreshToken(refreshToken: String): String {
        val claims = jwtParser.parseEncryptedClaims(refreshToken)
        val tokenType = claims.header[TOKEN_TYPE_HEADER_KEY] ?: throw RuntimeException()
        if (tokenType != REFRESH_TOKEN_TYPE_VALUE) throw RuntimeException()

        return claims.payload[USER_ID_CLAIM_KEY] as? String ?: throw RuntimeException()
    }

    private fun generateAccessTokenExpiration() = Date(System.currentTimeMillis() + appProperties.token.expiration.access * 1000)

    private fun generateRefreshTokenExpiration() = Date(System.currentTimeMillis() + appProperties.token.expiration.refresh * 1000)

    companion object {
        const val USER_ID_CLAIM_KEY = "user_id"
        const val AUTHORITIES_CLAIM_KEY = "authorities"

        const val TOKEN_TYPE_HEADER_KEY = "token_type"
        const val ACCESS_TOKEN_TYPE_VALUE = "access_token"
        const val REFRESH_TOKEN_TYPE_VALUE = "refresh_token"
    }
}
