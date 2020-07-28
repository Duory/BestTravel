package makov.besttravel.search.ui.search

import androidx.annotation.StringRes
import makov.besttravel.search.domain.model.Airport
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface CitySearchView: MvpView {
    @OneExecution
    fun showError(@StringRes stringRes: Int)
    @AddToEndSingle
    fun showSuggestions(suggestions: List<Airport>)
}