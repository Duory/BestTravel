package makov.besttravel.search.data.model

import com.google.gson.annotations.SerializedName

data class SuggestionsApiModel(
    @SerializedName("cities") val cities: List<CityApiModel>
)