package makov.besttravel.search.ui.main

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import makov.besttravel.R
import makov.besttravel.global.CoroutineScopedPresenter
import makov.besttravel.search.domain.SuggestionsInteractor
import makov.besttravel.search.domain.model.City
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val interactor: SuggestionsInteractor
): CoroutineScopedPresenter<MainView>() {

    private var searchFromJob: Job? = null
    private var searchToJob: Job? = null

    private var selectedFromCity: City? = null
    private var selectedToCity: City? = null

    fun onFromTextChanged(newText: String) {
        searchFromJob?.cancel()
        selectedFromCity = null
        searchFromJob = startSuggestionsJob(newText, viewState::showFromSuggestions)
    }

    fun onToTextChanged(newText: String) {
        searchToJob?.cancel()
        selectedToCity = null
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

    fun onFromCityPicked(fromCity: City?) {
        selectedFromCity = fromCity
    }

    fun onToCityPicked(toCity: City?) {
        selectedToCity = toCity
    }

    fun onSearchClicked() {
        (selectedFromCity to selectedToCity).let {(fromCity, toCity) ->
            if (fromCity != null && toCity != null) {
                viewState.navigateToSearchStart(fromCity, toCity)
            } else {

            }
        }
    }

    companion object {
        private const val SEARCH_DELAY_MS = 300L
    }
}