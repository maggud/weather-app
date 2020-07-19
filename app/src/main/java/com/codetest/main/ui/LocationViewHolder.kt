package com.codetest.main.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codetest.R
import com.codetest.main.model.Location
import com.codetest.main.model.Status
import kotlinx.android.synthetic.main.location.view.*


class LocationViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup): LocationViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.location, parent, false)
            return LocationViewHolder(view)
        }
    }

    fun setup(location: Location) {
        itemView.card.setCardBackgroundColor(getColor(location.status))
        itemView.name.text = location.name
        val weather = location.temperature + "°C " + String(Character.toChars(location.status.value))
        itemView.weatherInfo.text = weather
    }

    private fun getColor(status: Status): Int {
        return when (status) {
            Status.SUNNY, Status.MOSTLY_SUNNY, Status.PARTLY_SUNNY, Status.PARTLY_SUNNY_RAIN, Status.BARELY_SUNNY
            -> itemView.resources.getColor(R.color.blue)
            Status.THUNDER_CLOUD_AND_RAIN, Status.TORNADO, Status.LIGHTENING -> itemView.resources.getColor(R.color.red)
            Status.CLOUDY, Status.SNOW_CLOUD, Status.RAINY -> itemView.resources.getColor(R.color.grey)

        }
    }
}
