package makov.besttravel.application

import android.app.Application
import makov.besttravel.application.di.Di
import toothpick.Toothpick

class BestTravelApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initToothpick()
    }

    private fun initToothpick() {
        Di.init()
        Toothpick.inject(this, Di.openRootScope(this))
    }
}