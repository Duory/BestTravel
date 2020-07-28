package makov.besttravel.search.data.model

import makov.besttravel.search.domain.model.Airport

object CityToAirportsMapper {

    fun map(source: CityApiModel): List<Airport> {
        // distinct потому что апишка иногда возвращает дубликаты
        return source.iata.distinct().map {
            with(source) {
                Airport(city, it, location.latitude, location.longitude)
            }
        }
    }
}