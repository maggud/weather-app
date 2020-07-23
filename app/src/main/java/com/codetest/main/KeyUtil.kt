package com.codetest.main

import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.codetest.CodeTestApplication
import java.util.*

object KeyUtil {

    private const val KEY = "api_key"

    private val preferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(CodeTestApplication.appContext())

    fun getKey(): String {
        preferences.getString(KEY, null)?.let {
            return it
        } ?: run {
            val apiKey = UUID.randomUUID().toString()
            preferences.edit { putString(KEY, apiKey) }
            return apiKey
        }
    }
}