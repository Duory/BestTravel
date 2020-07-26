package makov.besttravel.search.data

import makov.besttravel.search.data.model.CityMapper
import makov.besttravel.search.domain.model.City
import javax.inject.Inject

class SuggestionsRepository @Inject constructor(
    private val api: SuggestionsApi
) {

    suspend fun getCitySuggestions(searchString: String): List<City> {
        return api.getSuggestions(searchString, "ru").cities.map(CityMapper::map)
    }
}