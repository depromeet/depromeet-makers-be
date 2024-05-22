package com.depromeet.makers.infrastructure.gateway

import com.depromeet.makers.domain.gateway.AlertGateway
import com.depromeet.makers.infrastructure.restapi.SlackAPI
import com.depromeet.makers.infrastructure.restapi.dto.SlackWebHookAttachment
import com.depromeet.makers.infrastructure.restapi.dto.SlackWebHookField
import com.depromeet.makers.infrastructure.restapi.dto.SlackWebHookRequest
import org.springframework.stereotype.Component

@Component
class AlertGatewayImpl(
    private val slackAPI: SlackAPI,
): AlertGateway {
    override fun sendError(elements: List<Pair<String, String>>) {
        val requestDto = SlackWebHookRequest(
            attachments = listOf(
                SlackWebHookAttachment(
                    color = "#FC3131",
                    title = "백엔드 에러",
                    text = "서비스 에러 발생 리포트",
                    fields = elements.map {
                        SlackWebHookField(
                            title = it.first,
                            value = it.second,
                            short = it.second.length < 20,
                        )
                    }
                ),
            ),
        )
        slackAPI.sendSlackWebHook(requestDto)
    }

    override fun sendInfo(elements: List<Pair<String, String>>) {
        val requestDto = SlackWebHookRequest(
            attachments = listOf(
                SlackWebHookAttachment(
                    color = "#78F5FF",
                    title = "백엔드 정보",
                    text = "백엔드 정보 리포트",
                    fields = elements.map {
                        SlackWebHookField(
                            title = it.first,
                            value = it.second,
                            short = it.second.length < 20,
                        )
                    }
                ),
            ),
        )
        slackAPI.sendSlackWebHook(requestDto)
    }
}
