package com.depromeet.makers.infrastructure.db.entity

import com.depromeet.makers.domain.model.Member
import jakarta.persistence.*

@Entity(name = "member")
class MemberEntity private constructor(
    @Id
    @Column(name = "id", length = 26, columnDefinition = "CHAR(26)", nullable = false)
    val id: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "passcord", nullable = true)
    var passCord: String?,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "member_id")
    var generations: Set<MemberGenerationEntity>,
) {

    fun toDomain() = Member(
        memberId = id,
        name = name,
        email = email,
        passCord = passCord,
        generations = generations
            .map(MemberGenerationEntity::toDomain)
            .toSet()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    companion object {
        fun fromDomain(member: Member) = with(member) {
            MemberEntity(
                id = memberId,
                name = name,
                email = email,
                passCord = passCord,
                generations = generations
                    .map { MemberGenerationEntity.fromDomain(memberId, it) }
                    .toSet()
            )
        }
    }
}
