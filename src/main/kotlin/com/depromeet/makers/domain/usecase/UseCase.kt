package com.depromeet.makers.domain.usecase

interface UseCase<I, O> {
    fun execute(input: I): O
}
