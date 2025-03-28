package com.depromeet.makers.domain

import com.depromeet.makers.domain.enums.MemberPosition
import com.depromeet.makers.domain.enums.MemberRole
import com.depromeet.makers.domain.enums.MemberStatus
import com.depromeet.makers.domain.vo.Image
import com.depromeet.makers.domain.vo.MemberLink
import com.depromeet.makers.domain.vo.TeamHistory
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("member")
class Member(
    @Id
    val id: ObjectId,
    var name: String,
    var email: String,
    var deviceToken: String?,
    var description: String?,
    var position: MemberPosition,
    var role: MemberRole,
    var status: MemberStatus,
    var profileImage: Image?,
    var teamHistory: List<TeamHistory>,
    var company: String?,
    var website: Set<MemberLink>,
    var skills: Set<String>,
) {
    companion object {
        fun create(
            name: String,
            email: String,
            deviceToken: String? = null,
            role: MemberRole = MemberRole.USER,
            position: MemberPosition,
            status: MemberStatus,
            teamHistory: List<TeamHistory>,
        ): Member {
            return Member(
                id = ObjectId(),
                name = name,
                email = email,
                deviceToken = deviceToken,
                description = null,
                position = position,
                role = role,
                status = status,
                profileImage = null,
                teamHistory = teamHistory,
                company = null,
                website = emptySet(),
                skills = emptySet(),
            )
        }
    }
}
