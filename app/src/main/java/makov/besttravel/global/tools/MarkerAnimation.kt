package makov.besttravel.global.tools

import android.animation.ValueAnimator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import makov.besttravel.global.tools.SphericalUtils.toDegree
import makov.besttravel.global.tools.SphericalUtils.toRadian
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


object MarkerAnimation {

    fun animateMarkerTo(
        marker: Marker,
        finalPosition: LatLng,
        latLngInterpolator: LatLngInterpolator
    ) {
        val startPosition = marker.position
        val startRotation = SphericalUtils.computeHeading(startPosition, finalPosition).toFloat()
        marker.rotation = startRotation
        val valueAnimator = ValueAnimator()
        valueAnimator.addUpdateListener { animation ->
            val v = animation.animatedFraction
            val newPosition =
                latLngInterpolator.interpolate(startPosition, finalPosition, v.toDouble())
            marker.rotation = SphericalUtils.computeHeading(marker.position, newPosition).toFloat()
            marker.position = newPosition

        }
        valueAnimator.setFloatValues(0f, 1f) // Ignored.
        valueAnimator.duration = 8000
        valueAnimator.start()
    }

}

interface LatLngInterpolator {
    fun interpolate(from: LatLng, to: LatLng, fraction: Double): LatLng
}

class Spherical : LatLngInterpolator {
    override fun interpolate(from: LatLng, to: LatLng, fraction: Double): LatLng {
        val fromLat: Double = from.latitude.toRadian()
        val fromLng: Double = from.longitude.toRadian()
        val toLat: Double = to.latitude.toRadian()
        val toLng: Double = to.longitude.toRadian()
        val cosFromLat: Double = cos(fromLat)
        val cosToLat: Double = cos(toLat)

        // Computes Spherical interpolation coefficients.
        val angle: Double = SphericalUtils.computeAngleBetween(from, to)
        val sinAngle: Double = sin(angle)
        if (sinAngle < 1E-6) {
            return LatLng(
                from.latitude + fraction * (to.latitude - from.latitude),
                from.longitude + fraction * (to.longitude - from.longitude)
            )
        }
        val a: Double = sin((1 - fraction) * angle) / sinAngle
        val b: Double = sin(fraction * angle) / sinAngle

        // Converts from polar to vector and interpolate.
        val x: Double = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng)
        val y: Double = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng)
        val z: Double = a * sin(fromLat) + b * sin(toLat)

        // Converts interpolated vector back to polar.
        val lat: Double = atan2(z, sqrt(x * x + y * y))
        val lng: Double = atan2(y, x)
        return LatLng(lat.toDegree(), lng.toDegree())
    }
}
