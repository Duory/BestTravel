package makov.besttravel.search.ui.requestprogress.map

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.res.ResourcesCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import makov.besttravel.R
import kotlin.math.roundToInt

object MarkerBuilder {

    fun getWaypointMarker(
        latLng: LatLng,
        markerTitle: String,
        resources: Resources
    ): MarkerOptions {
        return MarkerOptions()
            .position(latLng)
            .anchor(0.5f, 0.5f)
            .alpha(0.8f)
            .icon(BitmapDescriptorFactory.fromBitmap(
                getWaypointBitmap(
                    markerTitle,
                    resources
                )
            ))
    }

    private fun getWaypointBitmap(markerTitle: String, resources: Resources): Bitmap {
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
            color = ResourcesCompat.getColor(resources, R.color.map_marker_color, null)
        }

        val shapeStorkPaint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = ResourcesCompat.getFloat(resources, R.dimen.map_waypoint_edging_width)
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

    fun getPlaneMarker(latLng: LatLng): MarkerOptions {
        return MarkerOptions()
            .position(latLng)
            .anchor(0.5f, 0.5f)
            .zIndex(1f)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
    }

    fun GoogleMap.addWaypointMarker(
        latLng: LatLng,
        markerTitle: String,
        resources: Resources
    ): GoogleMap {
        addMarker(
            getWaypointMarker(
                latLng,
                markerTitle,
                resources
            )
        )
        return this
    }
}