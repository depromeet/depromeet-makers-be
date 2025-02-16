package com.depromeet.makers.domain.model

data class Place(
    val placeId: String,
    val name: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
) {
    fun update(
        placeId: String,
        address: String,
        longitude: Double,
        latitude: Double,
        name: String,
    ) = copy(
        placeId = placeId,
        address = address,
        longitude = longitude,
        latitude = latitude,
        name = name,
    )

    fun maskLocation(): Place {
        return copy(
            longitude = 0.0,
            latitude = 0.0,
        )
    }

    companion object {
        fun create(
            placeId: String,
            address: String,
            longitude: Double,
            latitude: Double,
            name: String,
        ) = Place(
            placeId = placeId,
            address = address,
            longitude = longitude,
            latitude = latitude,
            name = name,
        )
    }
}
