package makov.besttravel.search.ui.search

import makov.besttravel.global.mvp.ErrorView
import makov.besttravel.global.mvp.ProgressView
import makov.besttravel.search.domain.model.Airport
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface AirportSearchView: MvpView, ProgressView, ErrorView {
    @AddToEndSingle
    fun showSuggestions(suggestions: List<Airport>)
}