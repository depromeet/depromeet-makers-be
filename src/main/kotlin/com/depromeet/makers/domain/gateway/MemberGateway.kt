package com.depromeet.makers.domain.gateway

import com.depromeet.makers.domain.model.Member

interface MemberGateway {
    fun save(member: Member): Member
}
