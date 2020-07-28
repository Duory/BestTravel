package makov.besttravel.search.ui.requestprogress

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import makov.besttravel.R
import makov.besttravel.global.tools.MarkerAnimation
import makov.besttravel.global.tools.Spherical
import moxy.MvpAppCompatFragment
import kotlin.math.roundToInt


class RequestProgressFragment : MvpAppCompatFragment(R.layout.fragment_request_progress) {

    val args: RequestProgressFragmentArgs by navArgs()

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)!!

        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { map ->
            mapView.isClickable = false

            map.setMaxZoomPreference(3f)

            val fromCityLatLng = LatLng(args.fromCity.latitude, args.fromCity.longitude)
            val toCityLatLng = LatLng(args.toCity.latitude, args.toCity.longitude)

            map.moveCamera(
                CameraUpdateFactory.newLatLngBounds(
                    LatLngBounds.Builder()
                        .include(fromCityLatLng)
                        .include(toCityLatLng)
                        .build(),
                    // Для Lite Mode этот параметр не учитывается, что приводит к тому, что иногда маркер заступает за границу карты
                    // Если использовать карту не в Lite mode, то всё окей, но она дольше подгружается
                    resources.getDimensionPixelOffset(R.dimen.map_padding)
                )
            )
            map.moveCamera(CameraUpdateFactory.zoomOut())

            map.addMarker(
                MarkerOptions()
                    .position(fromCityLatLng)
                    .anchor(0.5f, getMarkerAnchorPoint(fromCityLatLng, toCityLatLng))
                    .alpha(0.8f)
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            getMarkerBitmap(
                                args.fromiata ?: args.fromCity.city,
                                args.fromiata != null
                            )
                        )
                    )
            )

            val planeMarker = map.addMarker(
                MarkerOptions()
                    .position(fromCityLatLng)
                    .anchor(0.5f, 0.5f)
                    .icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.ic_plane)
                    )
            )

            MarkerAnimation.animateMarkerTo(planeMarker, toCityLatLng, Spherical())

            map.addMarker(
                MarkerOptions()
                    .position(toCityLatLng)
                    .anchor(0.5f, getMarkerAnchorPoint(toCityLatLng, fromCityLatLng))
                    .alpha(0.8f)
                    .icon(
                        BitmapDescriptorFactory.fromBitmap(
                            getMarkerBitmap(
                                args.toiata ?: args.toCity.city,
                                args.toiata != null
                            )
                        )
                    )
            )

            map.addPolyline(
                PolylineOptions()
                    .add(fromCityLatLng, toCityLatLng)
                    .geodesic(true)
                    .width(5f)
                    .color(Color.RED)
            )
        }

        return rootView
    }

    private fun getMarkerAnchorPoint(first: LatLng, second: LatLng): Float {
        return if (first.latitude > second.latitude) {
            1f
        } else {
            0f
        }
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

    private fun getMarkerBitmap(markerTitle: String, isiata: Boolean): Bitmap {
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize =
                resources.getDimension(if (isiata) R.dimen.map_code_size else R.dimen.map_text_size)
        }
        val textWidth = textPaint.measureText(markerTitle)
        val markerHeight = resources.getDimensionPixelOffset(R.dimen.map_marker_height)
        val markerWidth = textWidth.roundToInt() + (resources.getDimensionPixelOffset(R.dimen.map_marker_horizontal_padding) * 2)
        val markerCornerRadius = markerHeight / 2f

        val shapeFillPaint = Paint().apply {
            color = ResourcesCompat.getColor(resources, R.color.map_marker_color, null)
        }

        val shapeStorkPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = 8f
            strokeCap = Paint.Cap.ROUND
        }

        val bitmap = Bitmap.createBitmap(markerWidth, markerHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawRoundRect(
            4f, 4f, markerWidth -4f, markerHeight -4f,
            markerCornerRadius, markerCornerRadius,
            shapeFillPaint
        )
        canvas.drawRoundRect(
            4f, 4f, markerWidth -4f, markerHeight -4f,
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
        return bitmap
    }
}