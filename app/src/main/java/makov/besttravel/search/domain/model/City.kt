package makov.besttravel.search.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val fullName: String,
    val city: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    override fun toString() = fullName
}