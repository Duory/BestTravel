package makov.besttravel.search.ui

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import makov.besttravel.R
import makov.besttravel.global.CoroutineScopedPresenter
import makov.besttravel.search.domain.SuggestionsInteractor
import makov.besttravel.search.domain.model.City
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val interactor: SuggestionsInteractor
): CoroutineScopedPresenter<SearchView>() {

    private var searchFromJob: Job? = null
    private var searchToJob: Job? = null

    fun onFromTextChanged(newText: String) {
        searchFromJob?.cancel()
        searchFromJob = startSuggestionsJob(newText, viewState::showFromSuggestions)
    }

    fun onToTextChanged(newText: String) {
        searchToJob?.cancel()
        searchToJob = startSuggestionsJob(newText, viewState::showToSuggestions)
    }

    private fun startSuggestionsJob(searchText: String, success: (List<City>) -> Unit): Job {
        return presenterScope.launch {
            runCatching {
                delay(SEARCH_DELAY_MS)
                success(interactor.getCitySuggestions(searchText))
            }.onFailure {
                if (it !is CancellationException) {
                    viewState.showError(R.string.error_default_msg)
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DELAY_MS = 1000L
    }
}