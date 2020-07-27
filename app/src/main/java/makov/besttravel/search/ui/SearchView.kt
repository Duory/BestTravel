package makov.besttravel.search.ui

import makov.besttravel.search.domain.model.City
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface SearchView: MvpView {

    @AddToEndSingle
    fun showFromSuggestions(suggestions: List<City>)
    @AddToEndSingle
    fun showToSuggestions(suggestions: List<City>)
}