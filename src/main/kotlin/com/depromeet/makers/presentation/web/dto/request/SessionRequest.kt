package com.depromeet.makers.presentation.web.dto.request

import com.depromeet.makers.domain.vo.SessionPlace
import java.time.LocalDateTime

data class SessionRequest(
    val generation: Int,
    val week: Int,
    val title: String,
    val description: String,
    val place: SessionPlace?,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
) {
    init {
        require(generation > 0) { "generation must be greater than 0" }
        require(week > 0) { "week must be greater than 0" }
    }
}

data class SessionGenerationRequest(
    val generation: Int,
)
