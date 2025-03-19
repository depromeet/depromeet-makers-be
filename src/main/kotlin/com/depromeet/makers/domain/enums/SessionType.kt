package com.depromeet.makers.domain.enums

import com.depromeet.makers.domain.Session

enum class SessionType {
    ONLINE,
    OFFLINE,
    ;

    companion object {
        fun of(session: Session): SessionType {
            return when (session.place) {
                null -> ONLINE
                else -> OFFLINE
            }
        }
    }
}
