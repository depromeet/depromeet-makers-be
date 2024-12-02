package com.depromeet.makers.domain.gateway

import com.depromeet.makers.domain.model.member.Member

interface MemberGateway {
    fun findByEmail(email: String): Member?
    fun getById(memberId: String): Member
    fun save(member: Member): Member
}
