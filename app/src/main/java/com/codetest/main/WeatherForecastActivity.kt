package com.codetest.main

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.codetest.R
import com.codetest.main.model.Location
import com.codetest.main.model.Status
import com.codetest.main.ui.AddLocationDialogFragment
import com.codetest.main.ui.LocationViewHolder
import kotlinx.android.synthetic.main.activity_main.*


class WeatherForecastActivity : AppCompatActivity() {

    private var adapter = ListAdapter()
    private var locations: List<Location> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.codetest.R.layout.activity_main)

        adapter = ListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        reportWeatherButton.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .let {
                    AddLocationDialogFragment()
                        .show(it, null)
                }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchLocations()
    }

    fun fetchLocations() {
        LocationHelper.getLocations { response ->
            if (response == null) {
                showError(getString(R.string.error_fetching_locations))
            } else {
                locations = response
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun showError(message: String = "") {
        AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.error_title))
            .setMessage(message)
            .setPositiveButton(resources.getString(R.string.ok), { _, _ -> })
            .create()
            .show()
    }

    private inner class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun getItemCount(): Int {
            return locations.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return LocationViewHolder.create(parent)
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            (viewHolder as? LocationViewHolder)?.setup(locations[position])
        }
    }
}