package makov.besttravel.search.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Airport(
    val city: String,
    val iata: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable