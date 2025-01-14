package com.depromeet.makers.domain.model

enum class MemberRole(
    val roleName: String
) {
    ORGANIZER("ROLE_ORGANIZER"),
    MEMBER("ROLE_MEMBER"),
    GRADUATE("ROLE_GRADUATE");
}
