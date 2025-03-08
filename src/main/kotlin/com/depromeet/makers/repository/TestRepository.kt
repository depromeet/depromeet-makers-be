package com.depromeet.makers.repository

import com.depromeet.makers.domain.Test
import org.springframework.data.mongodb.repository.MongoRepository

interface TestRepository: MongoRepository<Test, String> {
}
