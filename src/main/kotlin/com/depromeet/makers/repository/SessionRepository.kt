package com.depromeet.makers.repository

import com.depromeet.makers.domain.Session
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface SessionRepository : MongoRepository<Session, ObjectId> {
    fun findByGeneration(generation: Int): List<Session>

    fun findByGenerationAndWeek(generation: Int, week: Int): Session?

    fun deleteAllByGeneration(generation: Int)
}
