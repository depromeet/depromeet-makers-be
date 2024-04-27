package com.depromeet.makers.domain.use_case

import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.Member

class CreateNewMember(
    private val memberGateway: MemberGateway,
): UseCase<CreateNewMember.CreateNewMemberInput, Member> {
    data class CreateNewMemberInput(
        val name: String
    )

    override fun execute(input: CreateNewMemberInput): Member {
        val member = Member(
            memberId = "",
            name = input.name,
        )
        return memberGateway.save(member)
    }
}
