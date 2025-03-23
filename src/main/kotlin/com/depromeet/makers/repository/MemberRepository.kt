package com.depromeet.makers.repository

import com.depromeet.makers.domain.Member
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface MemberRepository : MongoRepository<Member, ObjectId>
