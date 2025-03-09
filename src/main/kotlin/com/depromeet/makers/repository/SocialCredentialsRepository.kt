package com.depromeet.makers.repository

import com.depromeet.makers.domain.SocialCredentials
import com.depromeet.makers.domain.SocialCredentialsKey
import org.springframework.data.mongodb.repository.MongoRepository

interface SocialCredentialsRepository : MongoRepository<SocialCredentials, SocialCredentialsKey>
