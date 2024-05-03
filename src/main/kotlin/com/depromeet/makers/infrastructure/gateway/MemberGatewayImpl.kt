package com.depromeet.makers.infrastructure.gateway

import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.infrastructure.db.entity.MemberEntity
import com.depromeet.makers.infrastructure.db.repository.JpaMemberRepository

import org.springframework.stereotype.Component

@Component
class MemberGatewayImpl(
    private val jpaMemberRepository: JpaMemberRepository,
) : MemberGateway {
    override fun findByEmail(email: String): Member? {
        return jpaMemberRepository
            .findByEmail(email)
            ?.let(MemberEntity::toDomain)
    }

    override fun getById(memberId: String): Member {
        return jpaMemberRepository
            .getReferenceById(memberId)
            .let(MemberEntity::toDomain)
    }

    override fun save(member: Member): Member {
        val memberEntity = MemberEntity.fromDomain(member)
        return jpaMemberRepository
            .save(memberEntity)
            .toDomain()
    }
}
