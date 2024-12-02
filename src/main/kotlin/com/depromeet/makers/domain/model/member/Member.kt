package com.depromeet.makers.domain.model.member

import com.depromeet.makers.util.generateULID

data class Member(
    val memberId: String,
    val name: String,
    val email: String,
    val passCord: String?,
    val generations: Set<MemberGeneration>,
) {
    fun hasPassCord() = passCord != null

    fun initializePassCord(passCord: String) = this.copy(
        passCord = passCord
    )

    companion object {
        fun newMember(
            name: String,
            email: String,
            initialGeneration: MemberGeneration,
        ) = Member(
            memberId = generateULID(),
            name = name,
            email = email,
            passCord = null,
            generations = setOf(initialGeneration)
        )
    }
}
