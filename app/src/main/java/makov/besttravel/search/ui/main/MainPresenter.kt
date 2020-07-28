package makov.besttravel.search.ui.main

import makov.besttravel.global.CoroutineScopedPresenter
import makov.besttravel.search.domain.model.Airport
import javax.inject.Inject

class MainPresenter @Inject constructor() : CoroutineScopedPresenter<MainView>() {
    private var selectedFromAirport: Airport? = null
    private var selectedToAirport: Airport? = null

    fun onSearchClicked() {
        (selectedFromAirport to selectedToAirport).let { (fromAirport, toAirport) ->
            if (fromAirport != null && toAirport != null) {
                viewState.navigateToSearchStart(fromAirport, toAirport)
            } else {

            }
        }
    }

    fun onFromAirportSelected(selectedAirport: Airport) {
        selectedFromAirport = selectedAirport
        viewState.showFromAirport(selectedAirport)
    }

    fun onToAirportSelected(selectedAirport: Airport) {
        selectedToAirport = selectedAirport
        viewState.showToAirport(selectedAirport)
    }
}