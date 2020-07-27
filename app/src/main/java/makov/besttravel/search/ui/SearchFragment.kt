package makov.besttravel.search.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import makov.besttravel.R
import makov.besttravel.databinding.FragmentSearchBinding
import makov.besttravel.global.tools.addOnTextChangedListener
import makov.besttravel.global.tools.viewBinding
import makov.besttravel.search.domain.model.City
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SearchFragment : MvpAppCompatFragment(R.layout.fragment_search), SearchView {

    @Inject
    lateinit var presenterProvider: Provider<SearchPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val binding by viewBinding(FragmentSearchBinding::bind)

    private val fromAdapter by lazy(this::getAdapter)
    private val toAdapter by lazy(this::getAdapter)

    private fun getAdapter(): NoFilterCitiesArrayAdapter {
        return NoFilterCitiesArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.from.addOnTextChangedListener(presenter::onFromTextChanged)
        binding.to.addOnTextChangedListener(presenter::onToTextChanged)
        binding.from.setAdapter(fromAdapter)
        binding.to.setAdapter(toAdapter)
    }

    override fun showFromSuggestions(suggestions: List<City>) {
        fromAdapter.submitData(suggestions)
    }

    override fun showToSuggestions(suggestions: List<City>) {
        toAdapter.submitData(suggestions)
    }

    override fun showError(@StringRes stringRes: Int) {
        Snackbar.make(requireView(), stringRes, Snackbar.LENGTH_LONG)
            .show()
    }
}