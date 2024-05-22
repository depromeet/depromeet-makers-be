package com.depromeet.makers.domain.gateway

interface AlertGateway {
    fun sendError(vararg elements: Pair<String, String>)
    fun sendInfo(vararg elements: Pair<String, String>)
}
