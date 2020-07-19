package com.codetest.main

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.codetest.CodeTestApplication
import java.util.*

class KeyUtil {

    companion object {
        const val KEY = "api_key"
    }

    private fun preferences(): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(CodeTestApplication.appContext())

    fun getKey(): String {
        preferences().getString(KEY, null)?.let {
            return it
        } ?: kotlin.run {
            val apiKey = UUID.randomUUID().toString()
            preferences()
                .edit()
                .putString(KEY, apiKey)
                .apply()
            return apiKey
        }
    }
}