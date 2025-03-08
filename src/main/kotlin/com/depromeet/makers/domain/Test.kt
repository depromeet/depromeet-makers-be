package com.depromeet.makers.domain

import com.depromeet.makers.domain.vo.Age
import org.springframework.data.mongodb.core.mapping.Document

@Document("test")
class Test(
    val id: String,
    val name: String,
    val age: Age,
) {

}
