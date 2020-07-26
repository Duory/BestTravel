package makov.besttravel.search.data.model

import com.google.gson.annotations.SerializedName

data class LocationApiModel(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double
)