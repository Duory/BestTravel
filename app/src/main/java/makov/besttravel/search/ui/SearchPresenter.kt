package makov.besttravel.search.ui

import makov.besttravel.global.CoroutineScopedPresenter
import makov.besttravel.search.domain.SuggestionsInteractor
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val interactor: SuggestionsInteractor
): CoroutineScopedPresenter<SearchView>() {
}