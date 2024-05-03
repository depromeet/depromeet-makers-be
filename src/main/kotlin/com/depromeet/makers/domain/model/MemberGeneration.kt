package com.depromeet.makers.domain.model

data class MemberGeneration(
    val generationId: Int,
    val groupId: Int?,
    val role: MemberRole,
    val position: MemberPosition,
) {
    fun hasGroup() = groupId != null
}

