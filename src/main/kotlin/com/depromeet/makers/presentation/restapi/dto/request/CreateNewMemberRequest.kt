package com.depromeet.makers.presentation.restapi.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema
data class CreateNewMemberRequest(
    val name: String,
)
