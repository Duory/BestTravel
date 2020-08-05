package makov.besttravel.search.ui.requestprogress.map

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import makov.besttravel.R
import makov.besttravel.global.tools.CubicBezier
import makov.besttravel.global.tools.Spherical
import makov.besttravel.search.domain.model.RouteType

object RouteBuilder {

    fun getRoute(
        from: LatLng,
        to: LatLng,
        routeType: RouteType,
        resources: Resources
    ): PolylineOptions {
        return PolylineOptions()
            .addAll(
                getRoutePoints(
                    from,
                    to,
                    routeType
                )
            )
            .width(ResourcesCompat.getFloat(resources, R.dimen.map_route_dot_size))
            .pattern(
                listOf(
                    Dot(),
                    Gap(ResourcesCompat.getFloat(resources, R.dimen.map_route_gap_size))
                )
            )
            .color(ResourcesCompat.getColor(resources, R.color.map_route_color, null))
    }

    private fun getRoutePoints(
        from: LatLng,
        to: LatLng,
        routeType: RouteType
    ): List<LatLng> {
        return when (routeType) {
            RouteType.GEODESIC -> Spherical()
            RouteType.CUBIC_BEZIER -> CubicBezier()
        }.let { interpolator ->
            (0..100).map {
                interpolator.interpolate(from, to, it * 0.01)
            }
        }
    }

    fun GoogleMap.addRoute(
        from: LatLng,
        to: LatLng,
        routeType: RouteType,
        resources: Resources
    ): GoogleMap {
        addPolyline(
            getRoute(
                from,
                to,
                routeType,
                resources
            )
        )
        return this
    }
}