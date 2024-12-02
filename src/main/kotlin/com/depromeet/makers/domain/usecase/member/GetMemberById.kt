package com.depromeet.makers.domain.usecase.member

import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.member.Member
import com.depromeet.makers.domain.usecase.UseCase

class GetMemberById(
    private val memberGateway: MemberGateway,
) : UseCase<GetMemberById.GetMemberByIdInput, Member> {
    data class GetMemberByIdInput(
        val memberId: String,
    )

    override fun execute(input: GetMemberByIdInput): Member {
        return memberGateway.getById(input.memberId)
    }
}
