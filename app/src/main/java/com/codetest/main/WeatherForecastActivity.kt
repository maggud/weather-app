package com.codetest.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codetest.R
import com.codetest.main.model.Location
import com.codetest.main.ui.AddLocationDialogFragment
import com.codetest.main.ui.LocationViewHolder
import kotlinx.android.synthetic.main.activity_main.*


class WeatherForecastActivity : AppCompatActivity() {

    private var adapter = LocationListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.codetest.R.layout.activity_main)

        adapter = LocationListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        reportWeatherButton.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .let {
                    AddLocationDialogFragment()
                        .show(it, null)
                }
        }

        swipeRefresh.setOnRefreshListener {
            fetchLocations()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchLocations(true)
    }

    fun fetchLocations(backgroundRefresh: Boolean = false) {
        swipeRefresh.isRefreshing = true
        LocationHelper.getLocations { response ->
            if (response == null) {
                if (!backgroundRefresh) {
                    showError(getString(R.string.error_fetching_locations))
                }
            } else {
                adapter.submitList(response)
            }
            swipeRefresh.isRefreshing = false
        }
    }

    private fun showError(message: String = "") {
        lifecycleScope.launchWhenResumed {
            AlertDialog.Builder(this@WeatherForecastActivity)
                .setTitle(resources.getString(R.string.error_title))
                .setMessage(message)
                .setPositiveButton(R.string.retry) { _, _ -> fetchLocations() }
                .setNeutralButton(resources.getString(R.string.ok)) { _, _ -> }
                .create()
                .show()
        }
    }

    private inner class LocationListAdapter :
        ListAdapter<Location, RecyclerView.ViewHolder>(LocationDiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return LocationViewHolder.create(parent).also {
                it.onDelete = { id ->
                    LocationHelper.deleteLocation(
                        id = id,
                        onSuccess = {
                            fetchLocations()
                        },
                        onError = {
                            Toast.makeText(this@WeatherForecastActivity, "Failed to delete. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            (viewHolder as? LocationViewHolder)?.setup(getItem(position))
        }
    }
}

object LocationDiffCallback : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean = oldItem == newItem
}