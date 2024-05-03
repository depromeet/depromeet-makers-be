package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.MemberAlreadyExistsException
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.Member
import com.depromeet.makers.domain.model.MemberGeneration
import com.depromeet.makers.domain.model.MemberPosition
import com.depromeet.makers.domain.model.MemberRole

class CreateNewMember(
    private val memberGateway: MemberGateway,
) : UseCase<CreateNewMember.CreateNewMemberInput, Member> {
    data class CreateNewMemberInput(
        val name: String,
        val email: String,
        val generationId: Int,
        val groupId: Int?,
        val role: MemberRole,
        val position: MemberPosition,
    )

    override fun execute(input: CreateNewMemberInput): Member {
        val previousMember = memberGateway.findByEmail(input.email)
        val newGeneration = MemberGeneration(
            generationId = input.generationId,
            role = input.role,
            position = input.position,
            groupId = input.groupId,
        )

        val newMember = if (previousMember != null) {
            // 이미 있으면 기존 멤버에 기수 데이터 추가
            if (previousMember.generations.any { it.generationId == newGeneration.generationId }) {
                throw MemberAlreadyExistsException()
            }

            val newGenerations = previousMember.generations + newGeneration
            previousMember.copy(
                generations = newGenerations
            )
        } else {
            // 없으면 새 멤버
            Member.newMember(
                name = input.name,
                email = input.email,
                initialGeneration = newGeneration,
            )
        }
        return memberGateway.save(newMember)
    }
}
