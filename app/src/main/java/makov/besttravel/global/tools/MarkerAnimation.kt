package makov.besttravel.global.tools

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.util.Property
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import kotlin.math.*


object MarkerAnimation {

    fun animateMarkerTo(
        marker: Marker,
        finalPosition: LatLng,
        latLngInterpolator: LatLngInterpolator
    ) {
        val typeEvaluator = TypeEvaluator<LatLng> { fraction, startValue, endValue ->
            latLngInterpolator.interpolate(
                startValue,
                endValue,
                fraction.toDouble()
            )
        }
        val property: Property<Marker, LatLng> =
            Property.of(Marker::class.java, LatLng::class.java, "position")
        val animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition)
        animator.duration = 10000
        animator.start()
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
        val angle: Double = computeAngleBetween(from, to)
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

    private fun computeAngleBetween(from: LatLng, to: LatLng): Double {
        return distanceRadians(
            from.latitude.toRadian(), from.longitude.toRadian(),
            to.latitude.toRadian(), to.longitude.toRadian()
        )
    }

    private fun distanceRadians(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        return arcHav(havDistance(lat1, lat2, lng1 - lng2))
    }

    private fun havDistance(lat1: Double, lat2: Double, dLng: Double): Double {
        return hav(lat1 - lat2) + hav(dLng) * cos(lat1) * cos(lat2)
    }

    private fun arcHav(x: Double) = 2 * asin(sqrt(x))

    private fun hav(x: Double) = sin(x * 0.5).pow(2)

    fun Double.toRadian(): Double = this / 180 * Math.PI
    fun Double.toDegree(): Double = this * 180 / Math.PI
}
