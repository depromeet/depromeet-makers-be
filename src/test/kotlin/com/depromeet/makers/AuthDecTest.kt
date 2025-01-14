package com.depromeet.makers

import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.domain.model.MemberGeneration
import com.depromeet.makers.domain.model.MemberPosition
import com.depromeet.makers.domain.model.MemberRole
import com.depromeet.makers.infrastructure.token.JWTTokenProvider
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority

class AuthDecTest(
) {
    val jwtTokenProvider: JWTTokenProvider = JWTTokenProvider(
        1000 * 60 * 60 * 24,
        1000 * 60 * 60 * 24 * 7
    )

    @Test
    fun create() {
        val accessToken = jwtTokenProvider.generateAccessToken(
            authentication = createTestUser()
        )

        println(accessToken)
    }

    @Test
    fun name() {
        val accessToken =
            "eyJ0b2tlbl90eXBlIjoiYWNjZXNzX3Rva2VuIiwiYWxnIjoiZGlyIiwiZW5jIjoiQTEyOENCQy1IUzI1NiJ9..Re9ZvO-SyI-UDRmdD2Lt_w.0_8L0fYADuZMKgwe1brcDxv3agz_uU3Cv4bqYlVpbH8EAoSyXnJzu7Y-dUrF3rBs0OvduumCbnwpslloiaT4LVPGCAyhZNj4m5QogDIvI9Y.wuKrDXBlBwNBODGc5atBxw"

        val result = jwtTokenProvider.parseAuthentication(accessToken)

        println(result.name)

        println(result)
    }

    private fun createTestUser(): Authentication {
        val member = Member(
            memberId = "test",
            name = "test",
            email = "test",
            passCord = "test",
            generations = setOf(
                MemberGeneration(
                    generationId = 14,
                    groupId = 1,
                    role = MemberRole.MEMBER,
                    position = MemberPosition.BACKEND,
                ),
                MemberGeneration(
                    generationId = 15,
                    groupId = 1,
                    role = MemberRole.ORGANIZER,
                    position = MemberPosition.BACKEND,
                )
            )
        )
        val authorities = member.generations.map { SimpleGrantedAuthority(it.role.roleName) }
        return UsernamePasswordAuthenticationToken(
            member.memberId,
            null,
            authorities,
        )
    }
}
