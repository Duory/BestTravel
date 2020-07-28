package makov.besttravel.global.tools

import com.google.android.gms.maps.model.LatLng
import kotlin.math.*


object SphericalUtils {

    fun computeHeading(from: LatLng, to: LatLng): Double {
        val fromLat: Double = from.latitude.toRadian()
        val fromLng: Double = from.longitude.toRadian()
        val toLat: Double = to.latitude.toRadian()
        val toLng: Double = to.longitude.toRadian()
        val dLng = toLng - fromLng
        val heading = atan2(
            sin(dLng) * cos(toLat),
            cos(fromLat) * sin(toLat) - sin(fromLat) * cos(toLat) * cos(dLng)
        )
        return wrap(heading.toDegree(), -180.0, 180.0)
    }

    fun wrap(n: Double, min: Double, max: Double): Double {
        return if (n >= min && n < max) n else (n - min).rem(max - min) + min
    }

    fun computeAngleBetween(from: LatLng, to: LatLng): Double {
        return distanceRadians(
            from.latitude.toRadian(), from.longitude.toRadian(),
            to.latitude.toRadian(), to.longitude.toRadian()
        )
    }

    fun distanceRadians(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        return arcHav(havDistance(lat1, lat2, lng1 - lng2))
    }

    fun havDistance(lat1: Double, lat2: Double, dLng: Double): Double {
        return hav(lat1 - lat2) + hav(dLng) * cos(lat1) * cos(lat2)
    }

    fun arcHav(x: Double) = 2 * asin(sqrt(x))

    fun hav(x: Double) = sin(x * 0.5).pow(2)

    fun Double.toRadian(): Double = this / 180 * Math.PI
    fun Double.toDegree(): Double = this * 180 / Math.PI
}