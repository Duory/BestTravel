package makov.besttravel.search.ui.main

import makov.besttravel.global.mvp.CoroutineScopedPresenter
import makov.besttravel.search.domain.model.Airport
import makov.besttravel.search.domain.model.RouteType
import javax.inject.Inject

class MainPresenter @Inject constructor() : CoroutineScopedPresenter<MainView>() {
    private var selectedFromAirport: Airport? = null
    private var selectedToAirport: Airport? = null
    private var selectedRouteType: RouteType = RouteType.GEODESIC

    fun onSearchClicked() {
        (selectedFromAirport to selectedToAirport).let { (fromAirport, toAirport) ->
            if (fromAirport != null && toAirport != null) {
                viewState.navigateToSearchStart(fromAirport, toAirport, selectedRouteType)
            }
        }
    }

    fun onFromAirportSelected(selectedAirport: Airport) {
        selectedFromAirport = selectedAirport
        viewState.showFromAirport(selectedAirport)
        updateFindButtonState()
    }

    fun onToAirportSelected(selectedAirport: Airport) {
        selectedToAirport = selectedAirport
        viewState.showToAirport(selectedAirport)
        updateFindButtonState()
    }

    private fun updateFindButtonState() {
        viewState.enableFindButton(
            selectedFromAirport != null
                    && selectedToAirport != null
                    && selectedToAirport != selectedToAirport
        )
    }

    fun onGeodesicChecked() {
        selectedRouteType = RouteType.GEODESIC
    }

    fun onCubicBezierChecked() {
        selectedRouteType = RouteType.CUBIC_BEZIER
    }
}