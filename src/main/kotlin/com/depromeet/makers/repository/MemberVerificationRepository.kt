package com.depromeet.makers.repository

import com.depromeet.makers.domain.MemberVerification
import com.depromeet.makers.domain.MemberVerificationKey
import org.springframework.data.mongodb.repository.MongoRepository

interface MemberVerificationRepository : MongoRepository<MemberVerification, MemberVerificationKey> {
}
