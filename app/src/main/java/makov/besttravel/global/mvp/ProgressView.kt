package makov.besttravel.global.mvp

import moxy.viewstate.strategy.alias.AddToEndSingle

interface ProgressView {
    @AddToEndSingle
    fun showProgress(inProgress: Boolean)
}