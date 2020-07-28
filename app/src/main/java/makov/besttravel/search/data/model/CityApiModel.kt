package makov.besttravel.search.data.model

import com.google.gson.annotations.SerializedName

data class CityApiModel(
    @SerializedName("fullname") val fullName: String,
    @SerializedName("city") val city: String,
    @SerializedName("location") val location: LocationApiModel
)
