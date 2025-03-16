package com.depromeet.makers.domain.vo

import kotlin.random.Random

@JvmInline
value class Code(
    val code: String,
) {
    fun mask(): Code {
        return Code("******")
    }

    companion object {
        fun generate(): Code {
            return Random.nextInt(100_000, 1_000_000).toString().let { Code(it) }
        }
    }
}