package makov.besttravel.search.ui

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import makov.besttravel.R
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}