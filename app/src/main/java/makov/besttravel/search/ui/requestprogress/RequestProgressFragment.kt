package makov.besttravel.search.ui.requestprogress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import makov.besttravel.R
import makov.besttravel.search.ui.requestprogress.map.CameraUtils.setupCamera
import makov.besttravel.search.ui.requestprogress.map.MapParentLifecycleObserver
import makov.besttravel.search.ui.requestprogress.map.MarkerBuilder
import makov.besttravel.search.ui.requestprogress.map.MarkerBuilder.addWaypointMarker
import makov.besttravel.search.ui.requestprogress.map.PlaneAnimator
import makov.besttravel.search.ui.requestprogress.map.RouteBuilder.addRoute


class RequestProgressFragment : Fragment() {

    val args: RequestProgressFragmentArgs by navArgs()

    private lateinit var mapView: MapView

    private val planeAnimator by lazy { PlaneAnimator(args.routeType) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_request_progress, container, false).also {
            mapView = it.findViewById(R.id.mapView)
            setupMap(savedInstanceState)
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        lifecycle.addObserver(MapParentLifecycleObserver(mapView))
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { map ->
            mapView.isClickable = false
            map.uiSettings.isRotateGesturesEnabled = false

            val fromLatLng = LatLng(args.fromCity.latitude, args.fromCity.longitude)
            val toLatLng = LatLng(args.toCity.latitude, args.toCity.longitude)

            map.addRoute(fromLatLng, toLatLng, args.routeType, resources)
                .addWaypointMarker(fromLatLng, args.fromCity.iata, resources)
                .addWaypointMarker(toLatLng, args.toCity.iata, resources)
                .setupCamera(savedInstanceState, resources, fromLatLng, toLatLng)
                .addMarker(MarkerBuilder.getPlaneMarker(fromLatLng)).also { planeMarker ->
                    planeAnimator.init(planeMarker, toLatLng, savedInstanceState)
                }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mapView.onSaveInstanceState(outState)
        planeAnimator.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}