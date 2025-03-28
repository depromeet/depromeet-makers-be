package com.depromeet.makers.domain.vo

import com.depromeet.makers.domain.enums.WebsiteType

data class MemberLink(
    val website: WebsiteType,
    val link: String,
)
