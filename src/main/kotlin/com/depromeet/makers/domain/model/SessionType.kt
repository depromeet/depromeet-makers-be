package com.depromeet.makers.domain.model

enum class SessionType {
    ONLINE, OFFLINE;

    fun isOnline() = this == ONLINE
}
