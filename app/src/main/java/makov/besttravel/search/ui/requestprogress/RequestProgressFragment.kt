package makov.besttravel.search.ui.requestprogress

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import makov.besttravel.R
import makov.besttravel.global.tools.CubicBezier
import makov.besttravel.global.tools.LatLngInterpolator
import makov.besttravel.global.tools.Spherical
import makov.besttravel.global.tools.SphericalUtils
import makov.besttravel.search.domain.model.RouteType
import kotlin.math.roundToInt


class RequestProgressFragment : Fragment() {

    val args: RequestProgressFragmentArgs by navArgs()

    private lateinit var mapView: MapView

    private val valueAnimator by lazy(::ValueAnimator)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_request_progress, container, false).also {
            mapView = it.findViewById(R.id.mapView)
            mapView.onCreate(savedInstanceState)

            mapView.getMapAsync { map ->
                mapView.isClickable = false
                map.setMaxZoomPreference(3f)
                map.uiSettings.isRotateGesturesEnabled = false

                val fromLatLng = LatLng(args.fromCity.latitude, args.fromCity.longitude)
                val toLatLng = LatLng(args.toCity.latitude, args.toCity.longitude)

                val interpolator = when (args.routeType) {
                    RouteType.GEODESIC -> Spherical()
                    RouteType.CUBIC_BEZIER -> CubicBezier()
                }

                // При смене конфигурации положение камеры сохраняется
                if (savedInstanceState == null) {
                    setupCameraBounds(fromLatLng, toLatLng, map)
                }

                addFromToMarkers(fromLatLng, toLatLng, args.fromCity.iata, args.toCity.iata, map)
                addRoute(fromLatLng, toLatLng, map, interpolator)

                val planeMarker = getPlaneMarkerAtStartPoint(fromLatLng, map)
                val savedAnimationFraction = savedInstanceState?.getFloat(SAVED_ANIMATION_FRACTION)
                animateMarkerTo(
                    valueAnimator,
                    savedAnimationFraction,
                    planeMarker,
                    toLatLng,
                    interpolator
                )
            }
        }
    }

    private fun setupCameraBounds(fromLatLng: LatLng, toLatLng: LatLng, map: GoogleMap) {
        map.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                LatLngBounds.Builder()
                    .include(fromLatLng)
                    .include(toLatLng)
                    .build(),
                resources.getDimensionPixelOffset(R.dimen.map_padding)
            )
        )
    }

    private fun addFromToMarkers(
        from: LatLng, to: LatLng,
        fromIata: String, toIata: String,
        map: GoogleMap
    ) {
        map.addMarker(
            MarkerOptions()
                .position(from)
                .anchor(0.5f, 0.5f)
                .alpha(0.8f)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(fromIata)))
        )
        map.addMarker(
            MarkerOptions()
                .position(to)
                .anchor(0.5f, 0.5f)
                .alpha(0.8f)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmap(toIata)))
        )
    }

    private fun getMarkerBitmap(markerTitle: String): Bitmap {
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = resources.getDimension(R.dimen.map_code_size)
        }
        val textWidth = textPaint.measureText(markerTitle)
        val markerHeight = resources.getDimensionPixelOffset(R.dimen.map_marker_height)
        val markerWidth =
            textWidth.roundToInt() + (resources.getDimensionPixelOffset(R.dimen.map_marker_horizontal_padding) * 2)
        val markerCornerRadius = markerHeight / 2f

        val shapeFillPaint = Paint().apply {
            color = ResourcesCompat.getColor(resources, R.color.secondaryDarkColor, null)
        }

        val shapeStorkPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 8f
            strokeCap = Paint.Cap.ROUND
        }

        return Bitmap.createBitmap(markerWidth, markerHeight, Bitmap.Config.ARGB_8888).also {
            val canvas = Canvas(it)

            val extraPadding = shapeStorkPaint.strokeWidth / 2

            canvas.drawRoundRect(
                extraPadding, extraPadding, markerWidth - extraPadding, markerHeight - extraPadding,
                markerCornerRadius, markerCornerRadius,
                shapeFillPaint
            )
            canvas.drawRoundRect(
                extraPadding, extraPadding, markerWidth - extraPadding, markerHeight - extraPadding,
                markerCornerRadius, markerCornerRadius,
                shapeStorkPaint
            )

            val textHeight = textPaint.fontMetrics.run { this.descent - this.ascent }
            canvas.drawText(
                markerTitle,
                (markerWidth - textWidth) / 2,
                (markerHeight / 2f) + (textHeight / 3),
                textPaint
            )
        }
    }

    private fun addRoute(
        from: LatLng,
        to: LatLng,
        map: GoogleMap,
        latLngInterpolator: LatLngInterpolator
    ) {
        map.addPolyline(
            PolylineOptions()
                .addAll(getRoutePoints(from, to, latLngInterpolator))
                .width(5f)
                .color(Color.RED)
        )
    }

    private fun getRoutePoints(
        from: LatLng,
        to: LatLng,
        latLngInterpolator: LatLngInterpolator
    ): List<LatLng> {
        return (0..100).map {
            latLngInterpolator.interpolate(from, to, it * 0.01)
        }
    }

    private fun getPlaneMarkerAtStartPoint(from: LatLng, map: GoogleMap): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(from)
                .anchor(0.5f, 0.5f)
                .zIndex(1f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
        )
    }

    private fun animateMarkerTo(
        valueAnimator: ValueAnimator,
        startFractionValue: Float?,
        marker: Marker,
        finalPosition: LatLng,
        latLngInterpolator: LatLngInterpolator
    ) {
        val startPosition = marker.position
        marker.rotation = SphericalUtils.computeHeading(startPosition, finalPosition).toFloat()
        valueAnimator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction
            val newPosition =
                latLngInterpolator.interpolate(startPosition, finalPosition, fraction.toDouble())
            marker.rotation = SphericalUtils.computeHeading(marker.position, newPosition).toFloat()
            marker.position = newPosition

        }
        valueAnimator.setFloatValues(0f, 1f)
        valueAnimator.duration = 8000

        // Чтобы восстановить анимацию при смене конфигурации. Это, конечно, не покрывает все кейсы,
        // но решение расширяемое
        if (startFractionValue != null) {
            valueAnimator.setCurrentFraction(startFractionValue)
        }
        valueAnimator.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mapView.onSaveInstanceState(outState)
        outState.putFloat(SAVED_ANIMATION_FRACTION, valueAnimator.animatedFraction)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val SAVED_ANIMATION_FRACTION = "SAVED_ANIMATION_FRACTION"
    }
}