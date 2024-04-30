package com.depromeet.makers.infrastructure.db.repository

import com.depromeet.makers.infrastructure.db.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface JpaMemberRepository: JpaRepository<MemberEntity, String> {
    fun findByEmail(email: String): MemberEntity?
}
