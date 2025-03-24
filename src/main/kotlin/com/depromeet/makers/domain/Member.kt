package com.depromeet.makers.domain

import com.depromeet.makers.domain.enums.MemberPosition
import com.depromeet.makers.domain.enums.MemberRole
import com.depromeet.makers.domain.enums.MemberStatus
import com.depromeet.makers.domain.vo.MemberLink
import com.depromeet.makers.domain.vo.TeamHistory
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("member")
class Member(
    @Id
    val id: ObjectId,
    val name: String,
    val email: String,
    val description: String?,
    val position: MemberPosition,
    val role: MemberRole,
    val status: MemberStatus,
    val profileImageUrl: String,
    val teamHistory: List<TeamHistory>,
    val company: String?,
    val website: Set<MemberLink>,
    val skills: Set<String>,
) {
    companion object {
        fun create(
            name: String,
            email: String,
            description: String?,
            position: MemberPosition,
            role: MemberRole,
            status: MemberStatus,
            profileImageUrl: String,
            teamHistory: List<TeamHistory>,
            company: String?,
            website: Set<MemberLink>,
            skills: Set<String>,
        ): Member {
            return Member(
                id = ObjectId(),
                name = name,
                email = email,
                description = description,
                position = position,
                role = role,
                status = status,
                profileImageUrl = profileImageUrl,
                teamHistory = teamHistory,
                company = company,
                website = website,
                skills = skills,
            )
        }
    }
}
