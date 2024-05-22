package com.depromeet.makers.infrastructure.restapi.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SlackWebHookRequest(
    @JsonProperty("attachments")
    val attachments: List<SlackWebHookAttachment>,
)

data class SlackWebHookAttachment(
    @JsonProperty("color")
    val color: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("text")
    val text: String,
    @JsonProperty("fields")
    val fields: List<SlackWebHookField>,
)

data class SlackWebHookField(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("value")
    val value: String,
    @JsonProperty("short")
    val short: Boolean,
)
