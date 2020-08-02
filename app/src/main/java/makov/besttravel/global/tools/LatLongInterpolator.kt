package makov.besttravel.global.tools

import com.google.android.gms.maps.model.LatLng
import makov.besttravel.global.tools.SphericalUtils.toDegree
import makov.besttravel.global.tools.SphericalUtils.toRadian
import kotlin.math.*

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

class CubicBezier : LatLngInterpolator {
    override fun interpolate(from: LatLng, to: LatLng, fraction: Double): LatLng {
        val centerPoint = getCenterPoint(from, to)
        val firstAdditionalPoint = thirdTrianglePoint(centerPoint, from)
        val secondAdditionalPoint = thirdTrianglePoint(centerPoint, to)

        return LatLng(
            cubicBezier(
                from.latitude,
                firstAdditionalPoint.latitude,
                secondAdditionalPoint.latitude,
                to.latitude,
                fraction
            ),
            cubicBezier(
                from.longitude,
                firstAdditionalPoint.longitude,
                secondAdditionalPoint.longitude,
                to.longitude,
                fraction
            )
        )
    }

    private fun getCenterPoint(first: LatLng, second: LatLng): LatLng {
        return LatLng(
            first.latitude + ((second.latitude - first.latitude) / 2),
            first.longitude + ((second.longitude - first.longitude) / 2)
        )
    }

    private fun thirdTrianglePoint(first: LatLng, second: LatLng): LatLng {
        val angle = 60.0.toRadian()
        val x1 = first.latitude
        val y1 = first.longitude
        val x2 = second.latitude
        val y2 = second.longitude
        val u = x2 - x1
        val v = y2 - y1
        val a3 = sqrt(u * u + v * v)
        val alp3 = Math.PI - angle - angle
        val a2 = a3 * sin(angle) / sin(alp3)
        val RHS1 = x1 * u + y1 * v + a2 * a3 * cos(angle)
        val RHS2 = y2 * u - x2 * v + a2 * a3 * sin(angle)
        val x3 = (1 / (a3 * a3)) * (u * RHS1 - v * RHS2)
        val y3 = (1 / (a3 * a3)) * (v * RHS1 + u * RHS2)
        return LatLng(x3, y3)
    }

    private fun cubicBezier(
        start: Double,
        firstAdditionalPoint: Double,
        secondAdditionalPoint: Double,
        end: Double,
        fraction: Double
    ): Double {
        return (1 - fraction).pow(3) * start +
                3 * fraction * (1 - fraction).pow(2) * firstAdditionalPoint +
                3 * fraction.pow(2) * (1 - fraction) * secondAdditionalPoint +
                fraction.pow(3) * end
    }
}