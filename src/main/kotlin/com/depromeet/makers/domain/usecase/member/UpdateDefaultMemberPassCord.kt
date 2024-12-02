package com.depromeet.makers.domain.usecase.member

import com.depromeet.makers.domain.exception.MemberNotFoundException
import com.depromeet.makers.domain.exception.PassCordAlreadySetException
import com.depromeet.makers.domain.gateway.MemberGateway
import com.depromeet.makers.domain.model.member.Member
import com.depromeet.makers.domain.usecase.UseCase

class UpdateDefaultMemberPassCord(
    private val memberGateway: MemberGateway,
    private val updateMemberPassCord: UpdateMemberPassCord,
) : UseCase<UpdateDefaultMemberPassCord.UpdateDefaultMemberPassCordInput, Member> {
    data class UpdateDefaultMemberPassCordInput(
        val email: String,
        val passCord: String,
    )

    override fun execute(input: UpdateDefaultMemberPassCordInput): Member {
        val member = memberGateway.findByEmail(input.email) ?: throw MemberNotFoundException()
        if (isPassCordInitialized(member)) throw PassCordAlreadySetException()

        return updateMemberPassCord.execute(
            UpdateMemberPassCord.UpdateMemberPassCordInput(
                memberId = member.memberId,
                passCord = input.passCord,
            )
        )
    }

    private fun isPassCordInitialized(member: Member) = member.hasPassCord()
}
