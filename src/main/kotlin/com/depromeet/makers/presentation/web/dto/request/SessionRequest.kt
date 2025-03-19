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
)
