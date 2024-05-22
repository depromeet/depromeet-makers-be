package com.depromeet.makers.infrastructure.restapi

import com.depromeet.makers.infrastructure.restapi.dto.SlackWebHookRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class SlackAPI(
    private val objectMapper: ObjectMapper,
    @Value("\${app.url.slack-webhook-url}") private val slackWebHookUrl: String,
) {
    @Async
    fun sendSlackWebHook(slackWebHookRequest: SlackWebHookRequest) {
        val payload = objectMapper.writeValueAsString(slackWebHookRequest)
        val requestEntity = RequestEntity.post(slackWebHookUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .body(payload)

        RestTemplate().exchange(requestEntity, Unit::class.java)
    }
}
