package makov.besttravel.search.ui

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import makov.besttravel.R
import makov.besttravel.application.viewBinding
import makov.besttravel.databinding.FragmentSearchBinding
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class SearchFragment : MvpAppCompatFragment(R.layout.fragment_search),
    SearchView {

    @Inject
    lateinit var presenterProvider: Provider<SearchPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    private val binding by viewBinding(FragmentSearchBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}