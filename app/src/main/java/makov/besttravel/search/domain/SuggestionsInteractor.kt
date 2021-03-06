package makov.besttravel.search.domain

import makov.besttravel.search.data.SuggestionsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SuggestionsInteractor @Inject constructor(
    private val repository: SuggestionsRepository
) {

    suspend fun getCitySuggestions(searchString: String) = repository.getAirportsSuggestions(searchString)
}