package com.depromeet.makers.domain.gateway

import com.depromeet.makers.domain.model.Member

interface MemberGateway {
    fun findByEmail(email: String): Member?
    fun save(member: Member): Member
}
