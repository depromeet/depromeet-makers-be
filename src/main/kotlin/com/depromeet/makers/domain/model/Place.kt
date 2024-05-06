package com.depromeet.makers.domain.model

data class Place(
    val address: String,
    val x: Double,
    val y: Double,
) {
    companion object {
        fun newPlace(
            address: String,
            x: Double,
            y: Double,
        ) = Place(
            address = address,
            x = x,
            y = y,
        )

        fun emptyPlace() = Place(
            address = "온라인",
            x = 0.0,
            y = 0.0,
        )
    }
}
