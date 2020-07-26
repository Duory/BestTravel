package makov.besttravel.search.data.model

import makov.besttravel.search.domain.model.City

object CityMapper {

    fun map(source: CityApiModel): City {
        return with(source) { City(fullName, location.latitude, location.longitude) }
    }
}