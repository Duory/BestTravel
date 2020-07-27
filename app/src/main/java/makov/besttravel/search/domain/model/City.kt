package makov.besttravel.search.domain.model

data class City(
    val fullName: String,
    val latitude: Double,
    val longitude: Double
) {
    override fun toString() = fullName
}