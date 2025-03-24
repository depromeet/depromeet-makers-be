package com.depromeet.makers.domain.vo

import com.depromeet.makers.domain.enums.Website

data class MemberLink(
    val website: Website,
    val link: String,
)
