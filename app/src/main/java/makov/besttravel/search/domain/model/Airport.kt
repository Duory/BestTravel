package makov.besttravel.search.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class Airport(
    val city: String,
    val iata: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable