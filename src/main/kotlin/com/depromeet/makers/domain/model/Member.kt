package com.depromeet.makers.domain.model

import com.depromeet.makers.util.generateULID

data class Member(
    val memberId: String,
    val name: String,
    val email: String,
    val passCord: String?,
    val generations: List<MemberGeneration>,
) {
    fun hasPassCord() = passCord != null

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
            generations = listOf(initialGeneration)
        )
    }
}
