package makov.besttravel.search.data.model

import com.google.gson.annotations.SerializedName

data class CityApiModel(
    @SerializedName("city") val city: String,
    @SerializedName("iata") val iata: List<String>,
    @SerializedName("location") val location: LocationApiModel
)
