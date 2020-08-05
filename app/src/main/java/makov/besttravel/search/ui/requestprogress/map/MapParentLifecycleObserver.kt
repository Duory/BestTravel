package makov.besttravel.search.ui.requestprogress.map

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.maps.MapView
import java.lang.ref.WeakReference

class MapParentLifecycleObserver(mapView: MapView): LifecycleObserver {

    private val mapViewRef = WeakReference(mapView)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        mapViewRef.get()?.onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mapViewRef.get()?.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mapViewRef.get()?.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        mapViewRef.get()?.onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mapViewRef.get()?.onDestroy()
    }
}