package com.depromeet.makers.domain.gateway

interface AlertGateway {
    fun sendError(elements: List<Pair<String, String>>)
    fun sendInfo(elements: List<Pair<String, String>>)
}
