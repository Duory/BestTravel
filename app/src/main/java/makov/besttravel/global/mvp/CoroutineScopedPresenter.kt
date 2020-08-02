package makov.besttravel.global.mvp

import androidx.annotation.CallSuper
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import moxy.MvpPresenter
import moxy.MvpView

abstract class CoroutineScopedPresenter<View: MvpView>: MvpPresenter<View>() {

    protected val presenterScope = MainScope()

    @CallSuper
    override fun onDestroy() {
        presenterScope.cancel()
    }
}