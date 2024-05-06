package com.depromeet.makers.domain.model

data class Place(
    val address: String,
    val longitude: Double,
    val latitude: Double,
) {
    companion object {
        fun newPlace(
            address: String,
            longitude: Double,
            latitude: Double,
        ) = Place(
            address = address,
            longitude = longitude,
            latitude = latitude,
        )

        fun emptyPlace() = Place(
            address = "온라인",
            longitude = 0.0,
            latitude = 0.0,
        )
    }
}
