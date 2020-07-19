package com.codetest.main

import com.codetest.main.api.LocationApiService
import com.codetest.main.model.Location
import java.util.*

class LocationHelper {

    companion object {

        fun getLocations(callback: (List<Location>?) -> Unit) {
            val locations: ArrayList<Location> = arrayListOf()
            val apiKey = KeyUtil().getKey()
            LocationApiService.getApi().get(apiKey, "locations", {
                val list = it.get("locations").asJsonArray
                for (json in list) {
                    locations.add(Location.from(json.asJsonObject))
                }
                callback(locations)
            }, {
                callback(null)
            })
        }
    }
}