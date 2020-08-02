package makov.besttravel.search.ui.search

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import makov.besttravel.R
import makov.besttravel.global.mvp.CoroutineScopedPresenter
import makov.besttravel.search.domain.SuggestionsInteractor
import makov.besttravel.search.domain.model.Airport
import javax.inject.Inject

class AirportSearchPresenter @Inject constructor(
    private val interactor: SuggestionsInteractor
) : CoroutineScopedPresenter<AirportSearchView>() {

    private var searchJob: Job? = null

    fun searchForText(newText: String) {
        searchJob?.cancel()
        searchJob = startSuggestionsJob(newText, viewState::showSuggestions)
    }

    private fun startSuggestionsJob(searchText: String, success: (List<Airport>) -> Unit): Job {
        return presenterScope.launch {
            kotlin.runCatching {
                delay(SEARCH_DELAY_MS)
                viewState.showProgress(true)
                success(interactor.getCitySuggestions(searchText))
                viewState.showProgress(false)
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