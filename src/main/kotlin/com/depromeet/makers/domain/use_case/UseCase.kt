package com.depromeet.makers.domain.use_case

interface UseCase<I, O> {
    fun execute(input: I): O
}
