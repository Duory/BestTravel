package makov.besttravel.search.data

import makov.besttravel.search.data.model.CityToAirportsMapper
import makov.besttravel.search.domain.model.Airport
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuggestionsRepository @Inject constructor(
    private val api: SuggestionsApi
) {

    suspend fun getAirportsSuggestions(searchString: String): List<Airport> {
        return api.getSuggestions(searchString, "ru")
            .cities
            .map { CityToAirportsMapper.map(it) }
            .flatten()
    }
}