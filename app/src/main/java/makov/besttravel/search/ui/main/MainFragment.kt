package makov.besttravel.search.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import makov.besttravel.R
import makov.besttravel.databinding.FragmentMainBinding
import makov.besttravel.global.tools.addOnCheckedListener
import makov.besttravel.global.tools.viewBinding
import makov.besttravel.search.domain.model.Airport
import makov.besttravel.search.domain.model.RouteType
import makov.besttravel.search.ui.search.AirportSearchFragment
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainFragment : MvpAppCompatFragment(R.layout.fragment_main),
    MainView {

    @Inject
    lateinit var presenterProvider: Provider<MainPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fromLayout.setOnClickListener { navigateToSearchAirport(AirportSearchFragment.FROM_REQUEST_KEY) }
        binding.toLayout.setOnClickListener { navigateToSearchAirport(AirportSearchFragment.TO_REQUEST_KEY) }
        binding.search.setOnClickListener { presenter.onSearchClicked() }
        binding.geodesic.addOnCheckedListener(presenter::onGeodesicChecked)
        binding.cubicBezier.addOnCheckedListener(presenter::onCubicBezierChecked)
    }

    override fun navigateToSearchStart(
        fromAirport: Airport,
        toAirport: Airport,
        selectedRouteType: RouteType
    ) {
        findNavController().navigate(
            MainFragmentDirections.requestProgressFragment(
                fromAirport,
                toAirport,
                selectedRouteType
            )
        )
    }

    override fun showFromAirport(airport: Airport) {
        binding.fromName.text = airport.city
        binding.fromIdata.text = airport.iata
    }

    override fun showToAirport(airport: Airport) {
        binding.toName.text = airport.city
        binding.toIdata.text = airport.iata
    }

    override fun enableFindButton(isEnabled: Boolean) {
        binding.search.isEnabled = isEnabled
    }

    private fun navigateToSearchAirport(requestKey: String) {
        setFragmentResultListener(requestKey) { responseRequestKey, bundle ->
            bundle.getParcelable<Airport>(AirportSearchFragment.RESULT_AIRPORT_KEY)?.let {
                when (responseRequestKey) {
                    AirportSearchFragment.FROM_REQUEST_KEY -> presenter.onFromAirportSelected(it)
                    AirportSearchFragment.TO_REQUEST_KEY -> presenter.onToAirportSelected(it)
                }
            }
        }
        findNavController().navigate(MainFragmentDirections.airportSearchFragment(requestKey))
    }
}