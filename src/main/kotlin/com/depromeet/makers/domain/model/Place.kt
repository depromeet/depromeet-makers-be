package com.depromeet.makers.domain.model

data class Place(
    val address: String,
    val name: String?,
    val longitude: Double,
    val latitude: Double,
) {
    fun update(
        address: String,
        longitude: Double,
        latitude: Double,
        name: String?,
    ) = copy(
        address = address,
        longitude = longitude,
        latitude = latitude,
        name = name,
    )

    companion object {
        fun newPlace(
            address: String,
            longitude: Double,
            latitude: Double,
            name: String?,
        ) = Place(
            address = address,
            longitude = longitude,
            latitude = latitude,
            name = name,
        )

        fun emptyPlace() = Place(
            address = "온라인",
            longitude = 0.0,
            latitude = 0.0,
            name = null,
        )
    }
}
