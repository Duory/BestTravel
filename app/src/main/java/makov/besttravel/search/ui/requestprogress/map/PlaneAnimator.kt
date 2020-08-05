package makov.besttravel.search.ui.requestprogress.map

import android.animation.ValueAnimator
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import makov.besttravel.global.tools.CubicBezier
import makov.besttravel.global.tools.Spherical
import makov.besttravel.global.tools.SphericalUtils
import makov.besttravel.search.domain.model.RouteType

class PlaneAnimator(routeType: RouteType) {

    private val interpolator = when (routeType) {
        RouteType.GEODESIC -> Spherical()
        RouteType.CUBIC_BEZIER -> CubicBezier()
    }

    private val valueAnimator by lazy(::ValueAnimator)

    fun init(marker: Marker, finalPosition: LatLng, savedInstanceState: Bundle?) {
        val startPosition = marker.position
        valueAnimator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction
            val newPosition =
                interpolator.interpolate(startPosition, finalPosition, fraction.toDouble())
            marker.rotation = SphericalUtils.computeHeading(
                if (fraction != 1f) {
                    marker.position
                } else {
                    interpolator.interpolate(startPosition, finalPosition, fraction - 0.01)
                },
                newPosition
            ).toFloat()
            marker.position = newPosition
        }
        valueAnimator.setFloatValues(0f, 1f)
        valueAnimator.duration = 8000

        // Чтобы восстановить анимацию при смене конфигурации. Это, конечно, не покрывает все кейсы,
        // но решение расширяемое
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_ANIMATION_FRACTION)) {
            valueAnimator.setCurrentFraction(savedInstanceState.getFloat(SAVED_ANIMATION_FRACTION))
        }
        valueAnimator.start()
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat(SAVED_ANIMATION_FRACTION, valueAnimator.animatedFraction)
    }

    companion object {
        private const val SAVED_ANIMATION_FRACTION = "SAVED_ANIMATION_FRACTION"
    }
}