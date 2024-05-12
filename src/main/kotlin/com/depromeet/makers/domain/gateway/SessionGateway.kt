package com.depromeet.makers.domain.gateway

import com.depromeet.makers.domain.model.Session

interface SessionGateway {
    fun save(session: Session): Session

    fun existsByGenerationAndWeek(generation: Int, week: Int): Boolean
}
