package com.depromeet.makers.domain.usecase

import com.depromeet.makers.domain.exception.PassCordAlreadySetException
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.Member

class UpdateDefaultMemberPassCord(
    private val memberGateway: MemberGateway,
    private val updateMemberPassCord: UpdateMemberPassCord,
) : UseCase<UpdateDefaultMemberPassCord.UpdateDefaultMemberPassCordInput, Member> {
    data class UpdateDefaultMemberPassCordInput(
        val memberId: String,
        val passCord: String,
    )

    override fun execute(input: UpdateDefaultMemberPassCordInput): Member {
        val member = memberGateway.getById(input.memberId)
        if (member.hasPassCord()) throw PassCordAlreadySetException()

        return updateMemberPassCord.execute(
            UpdateMemberPassCord.UpdateMemberPassCordInput(
                memberId = input.memberId,
                passCord = input.passCord,
            )
        )
    }
}
