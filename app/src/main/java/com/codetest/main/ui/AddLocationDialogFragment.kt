package com.codetest.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.codetest.R
import com.codetest.main.LocationHelper
import com.codetest.main.model.Status
import kotlinx.android.synthetic.main.dialog_add_location.*

class AddLocationDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_Alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_add_location, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle(R.string.report_weather_btn)

        val dropdownAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item)
                .apply {
                    addAll(Status.values().map { it.name })
                }
        statusDropdown.setAdapter(dropdownAdapter)

        submitButton.setOnClickListener {
            validateAndSubmit()
        }
    }

    private fun validateAndSubmit() {
        val cityName = cityName.text.toString().takeIf { it.isNotBlank() }
        val temperature = temperature.text.toString().takeIf { it.isNotBlank() }?.toIntOrNull()
        val status =
            statusDropdown.text.toString().takeIf { it.isNotBlank() }?.let { Status.from(it) }

        if (cityName != null && temperature != null && status != null) {
            LocationHelper.postLocation(
                name = cityName,
                temperature = temperature,
                status = status
            ) { error ->
                context?.let { context ->
                    Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
            // TODO: Make interface between activity and dialog so we can fetch new locations.
            dismiss()
        } else {
            // TODO: Make interface between activity and dialog and trigger error from there.
            Toast.makeText(
                context,
                "Invalid input. Set a name, status and temperature.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}