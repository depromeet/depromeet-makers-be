package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.Member

class UpdateMemberPassCord(
    private val memberGateway: MemberGateway,
) : UseCase<UpdateMemberPassCord.UpdateMemberPassCordInput, Member> {
    data class UpdateMemberPassCordInput(
        val memberId: String,
        val passCord: String,
    )

    override fun execute(input: UpdateMemberPassCordInput): Member {
        val member = memberGateway.getById(input.memberId)
        return memberGateway.save(member.copy(passCord = input.passCord))
    }
}
