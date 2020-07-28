package makov.besttravel.search.ui.main

import androidx.annotation.StringRes
import makov.besttravel.search.domain.model.City
import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

interface MainView: MvpView {

    @OneExecution
    fun showFromSuggestions(suggestions: List<City>)
    @OneExecution
    fun showToSuggestions(suggestions: List<City>)
    @OneExecution
    fun showError(@StringRes stringRes: Int)
    @OneExecution
    fun navigateToSearchStart(fromCity: City, toCity: City)
}