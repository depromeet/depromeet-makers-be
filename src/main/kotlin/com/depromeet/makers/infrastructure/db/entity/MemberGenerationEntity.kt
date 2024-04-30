package com.depromeet.makers.infrastructure.db.entity

import com.depromeet.makers.domain.model.MemberGeneration
import com.depromeet.makers.domain.model.MemberPosition
import com.depromeet.makers.domain.model.MemberRole
import jakarta.persistence.*
import java.io.Serializable

@IdClass(MemberGenerationEntityKey::class)
@Entity
class MemberGenerationEntity private constructor (
    @Id
    @Column(name = "member_id" , length = 26, columnDefinition = "CHAR(26)", nullable = false)
    val memberId: String,

    @Id
    @Column(name = "generation_id", nullable = false)
    val generationId: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: MemberRole,

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    val position: MemberPosition,
) {
    fun toDomain() = MemberGeneration(
        generationId = generationId,
        role = role,
        position = position
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MemberGenerationEntity

        if (memberId != other.memberId) return false
        if (generationId != other.generationId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = memberId.hashCode()
        result = 31 * result + generationId
        return result
    }

    companion object {
        fun fromDomain(memberId: String, memberGeneration: MemberGeneration) = with(memberGeneration) {
            MemberGenerationEntity(
                memberId = memberId,
                generationId = generationId,
                role = role,
                position = position
            )
        }
    }
}

class MemberGenerationEntityKey(
    val memberId: String,
    val generationId: Int,
): Serializable
