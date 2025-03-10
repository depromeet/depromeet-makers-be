package com.depromeet.makers.domain

import com.depromeet.makers.domain.enums.MemberRole
import com.depromeet.makers.domain.vo.Age
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("test")
class Member(
    @Id
    val id: String?,
    val name: String,
    val age: Age,
    val role: MemberRole,
) {
    companion object {
        fun create(name: String, age: Age): Member {
            return Member(
                id = null,
                name = name,
                age = age,
                role = MemberRole.USER,
            )
        }
    }
}
