package com.depromeet.makers.domain

import com.depromeet.makers.domain.enums.SessionType
import com.depromeet.makers.domain.vo.Code
import com.depromeet.makers.domain.vo.SessionPlace
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("session")
class Session(
    @Id
    val id: ObjectId,
    var generation: Int,
    var week: Int,
    var title: String,
    var description: String,
    var place: SessionPlace?,
    var code: Code = Code.generate(),
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
) {
    fun getType(): SessionType {
        return SessionType.of(this)
    }

    companion object {
        fun create(
            generation: Int,
            week: Int,
            title: String,
            description: String,
            place: SessionPlace?,
            startTime: LocalDateTime,
            endTime: LocalDateTime,
        ): Session {
            return Session(
                id = ObjectId(),
                generation = generation,
                week = week,
                title = title,
                description = description,
                place = place,
                startTime = startTime,
                endTime = endTime,
            )
        }
    }
}
