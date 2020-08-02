package makov.besttravel.search.ui.main

import makov.besttravel.search.domain.model.Airport
import makov.besttravel.search.domain.model.RouteType
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution

interface MainView : MvpView {
    @OneExecution
    fun navigateToSearchStart(
        fromAirport: Airport,
        toAirport: Airport,
        selectedRouteType: RouteType
    )

    @AddToEndSingle
    fun showFromAirport(airport: Airport)

    @AddToEndSingle
    fun showToAirport(airport: Airport)

    @AddToEndSingle
    fun enableFindButton(isEnabled: Boolean)
}