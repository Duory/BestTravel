package makov.besttravel.global.mvp

import androidx.annotation.StringRes
import moxy.viewstate.strategy.alias.AddToEndSingle

interface ErrorView {
    @AddToEndSingle
    fun showError(@StringRes errorResId: Int)
}