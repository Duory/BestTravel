package makov.besttravel.search.ui.search

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import makov.besttravel.R
import makov.besttravel.global.CoroutineScopedPresenter
import makov.besttravel.search.domain.SuggestionsInteractor
import makov.besttravel.search.domain.model.Airport
import javax.inject.Inject

class CitySearchPresenter @Inject constructor(
    private val interactor: SuggestionsInteractor
): CoroutineScopedPresenter<CitySearchView>() {

    private var searchJob: Job? = null

    fun searchForText(newText: String) {
        searchJob?.cancel()
        searchJob = startSuggestionsJob(newText, viewState::showSuggestions)
    }

    private fun startSuggestionsJob(searchText: String, success: (List<Airport>) -> Unit): Job {
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
        private const val SEARCH_DELAY_MS = 300L
    }
}