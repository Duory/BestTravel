package makov.besttravel.search.ui.main

import makov.besttravel.search.domain.model.Airport
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface MainView: MvpView {
    @OneExecution
    fun navigateToSearchStart(fromAirport: Airport, toAirport: Airport)
    @AddToEndSingle
    fun showFromAirport(airport: Airport)
    @AddToEndSingle
    fun showToAirport(airport: Airport)
}