package makov.besttravel.search.ui.search

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import makov.besttravel.R
import makov.besttravel.databinding.FragmentAirportSearchBinding
import makov.besttravel.global.tools.addOnTextChangedListener
import makov.besttravel.global.tools.openKeyboard
import makov.besttravel.global.tools.viewBinding
import makov.besttravel.search.domain.model.Airport
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class AirportSearchFragment : MvpAppCompatFragment(R.layout.fragment_airport_search),
    AirportSearchView {

    val args: AirportSearchFragmentArgs by navArgs()

    @Inject
    lateinit var presenterProvider: Provider<AirportSearchPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val binding by viewBinding(FragmentAirportSearchBinding::bind)

    private val adapter by lazy { AirportRecyclerViewAdapter { sendResult(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.list.adapter = adapter
        openKeyboard(binding.search)
        binding.search.addOnTextChangedListener { presenter.searchForText(it) }
    }

    override fun showProgress(inProgress: Boolean) {
        binding.progressCircular.isVisible = inProgress
    }

    override fun showError(@StringRes errorResId: Int) {
        Snackbar.make(requireView(), errorResId, Snackbar.LENGTH_LONG).show()
    }

    override fun showSuggestions(suggestions: List<Airport>) {
        adapter.submitList(suggestions)
        binding.emptyView.isVisible = suggestions.isEmpty()
    }

    private fun sendResult(airport: Airport) {
        setFragmentResult(args.requestKey, bundleOf(RESULT_AIRPORT_KEY to airport))
        findNavController().navigateUp()
    }

    companion object {
        const val FROM_REQUEST_KEY = "FROM_REQUEST_KEY"
        const val TO_REQUEST_KEY = "TO_REQUEST_KEY"
        const val RESULT_AIRPORT_KEY = "RESULT_AIRPORT_KEY"
    }
}