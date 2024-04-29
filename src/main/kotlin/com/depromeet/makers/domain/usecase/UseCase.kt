package com.depromeet.makers.domain.usecase

fun interface UseCase<I, O> {
    fun execute(input: I): O
}
