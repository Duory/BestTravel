package makov.besttravel.search.ui

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import makov.besttravel.global.CoroutineScopedPresenter
import makov.besttravel.search.domain.SuggestionsInteractor
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val interactor: SuggestionsInteractor
): CoroutineScopedPresenter<SearchView>() {

    private var searchFromJob: Job? = null
    private var searchToJob: Job? = null

    fun onFromTextChanged(newText: String) {
        searchFromJob?.cancel()
        searchFromJob = presenterScope.launch {
            delay(SEARCH_DELAY_MS)
            val suggestions = interactor.getCitySuggestions(newText)
            viewState.showFromSuggestions(suggestions)
        }
    }

    fun onToTextChanged(newText: String) {
        searchToJob?.cancel()
        searchToJob = presenterScope.launch {
            delay(SEARCH_DELAY_MS)
            val suggestions = interactor.getCitySuggestions(newText)
            viewState.showToSuggestions(suggestions)
        }
    }

    companion object {
        private const val SEARCH_DELAY_MS = 500L
    }
}