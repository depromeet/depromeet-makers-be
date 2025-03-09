package com.depromeet.makers.repository

import com.depromeet.makers.domain.Member
import org.springframework.data.mongodb.repository.MongoRepository

interface MemberRepository : MongoRepository<Member, String>
