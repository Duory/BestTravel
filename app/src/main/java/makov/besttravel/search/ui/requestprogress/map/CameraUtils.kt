package makov.besttravel.search.ui.requestprogress.map

import android.content.res.Resources
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import makov.besttravel.R

object CameraUtils {

    fun GoogleMap.setupCamera(
        savedInstanceState: Bundle? = null,
        resources: Resources,
        vararg waypoints: LatLng
    ): GoogleMap {
        if (savedInstanceState == null) {
            moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds.Builder().also { bounds ->
                        waypoints.forEach {
                            bounds.include(it)
                        }
                    }.build(),
                    resources.getDimensionPixelOffset(R.dimen.map_padding)
                )
            )
        }
        return this
    }
}