package makov.besttravel.search.ui

import makov.besttravel.search.domain.SuggestionsInteractor
import moxy.MvpPresenter
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val interactor: SuggestionsInteractor
): MvpPresenter<SearchView>() {
}