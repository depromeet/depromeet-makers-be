package com.depromeet.makers.infrastructure.db.entity

import com.depromeet.makers.domain.model.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "member")
data class MemberEntity(
    @Id
    @Column(name = "id")
    val id: String,

    @Column(name = "name")
    val name: String,
) {
    fun toDomain() = Member(
        memberId = id,
        name = name,
    )

    companion object {
        fun fromDomain(member: Member) = with(member) {
            MemberEntity(
                id = memberId,
                name = name,
            )
        }
    }
}
