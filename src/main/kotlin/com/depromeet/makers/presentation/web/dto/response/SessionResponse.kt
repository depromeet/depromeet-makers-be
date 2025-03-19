package com.depromeet.makers.presentation.web.dto.response

import com.depromeet.makers.domain.Session
import com.depromeet.makers.domain.enums.SessionType
import com.depromeet.makers.domain.vo.Code
import com.depromeet.makers.domain.vo.SessionPlace
import java.time.LocalDateTime

data class SessionResponse(
    val sessionId: String,
    val generation: Int,
    val week: Int,
    val title: String,
    val description: String,
    val type: SessionType,
    val place: SessionPlace?,
    val code: Code,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
) {
    companion object {
        fun from(session: Session): SessionResponse {
            return SessionResponse(
                sessionId = session.id.toHexString(),
                generation = session.generation,
                week = session.week,
                title = session.title,
                description = session.description,
                type = session.getType(),
                place = session.place,
                startTime = session.startTime,
                code = session.code,
                endTime = session.endTime,
            )
        }

        fun from(session: Session, mask: Boolean): SessionResponse {
            return when (mask) {
                false -> from(session)
                true -> from(session).copy(
                    code = session.code.mask()
                )
            }
        }
    }
}
