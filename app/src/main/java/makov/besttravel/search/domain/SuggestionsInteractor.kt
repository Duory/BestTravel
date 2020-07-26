package makov.besttravel.search.domain

import makov.besttravel.search.data.SuggestionsRepository
import javax.inject.Inject

class SuggestionsInteractor @Inject constructor(
    private val repository: SuggestionsRepository
) {

    suspend fun getCitySuggestions(searchString: String) = repository.getCitySuggestions(searchString)
}