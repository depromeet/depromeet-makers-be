package com.depromeet.makers.domain

import com.depromeet.makers.domain.enums.SocialCredentialsProvider
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "socialCredentials")
class SocialCredentials(
    @Id
    val key: SocialCredentialsKey,
    @DBRef
    val member: Member,
)

data class SocialCredentialsKey(
    val provider: SocialCredentialsProvider,
    val externalIdentifier: String,
)
