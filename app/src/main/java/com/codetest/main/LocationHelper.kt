package com.codetest.main

import com.codetest.main.api.LocationApiService
import com.codetest.main.model.Location
import com.codetest.main.model.LocationDto
import com.codetest.main.model.Status
import java.util.*

class LocationHelper {

    companion object {

        fun getLocations(callback: (List<Location>?) -> Unit) {
            val locations: ArrayList<Location> = arrayListOf()
            LocationApiService.getApi().get("locations", {
                val list = it.get("locations").asJsonArray
                for (json in list) {
                    locations.add(Location.from(json.asJsonObject))
                }
                callback(locations)
            }, {
                callback(null)
            })
        }

        fun postLocation(
            id: String? = null,
            name: String,
            status: Status,
            temperature: Int,
            onSuccess: () -> Unit = {},
            onError: (Throwable) -> Unit
        ) {
            val location = LocationDto(
                id = id,
                name = name,
                status = status.name,
                temperature = temperature
            )

            LocationApiService.getApi().postLocation(location, onSuccess, onError)
        }
    }
}