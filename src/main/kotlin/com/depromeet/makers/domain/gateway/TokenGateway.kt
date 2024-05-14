package com.depromeet.makers.domain.gateway

import com.depromeet.makers.domain.model.Member

interface TokenGateway {
    fun generateAccessToken(member: Member): String
    fun generateRefreshToken(member: Member): String
    fun extractMemberIdFromRefreshToken(token: String): String
}
