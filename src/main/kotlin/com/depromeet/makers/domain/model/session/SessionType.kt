package com.depromeet.makers.domain.model.session

enum class SessionType {
    ONLINE, OFFLINE;

    fun isOnline() = this == ONLINE

    fun isOffline() = this == OFFLINE
}
