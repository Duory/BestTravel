package makov.besttravel.search.data

import makov.besttravel.search.data.model.SuggestionsApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface SuggestionsApi {

    @GET("autocomplete")
    suspend fun getSuggestions(
        @Query("term") searchString: String,
        @Query("lang") langCode: String
    ): SuggestionsApiModel
}