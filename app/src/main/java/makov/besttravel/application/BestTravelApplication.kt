package makov.besttravel.application

import android.app.Application
import com.google.android.gms.maps.MapsInitializer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BestTravelApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this)
    }
}