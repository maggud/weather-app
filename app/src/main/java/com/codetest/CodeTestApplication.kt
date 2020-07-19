package com.codetest

import android.app.Application
import android.content.Context

class CodeTestApplication : Application() {
    companion object {
        private var context: Context? = null
        fun appContext(): Context? {
            return CodeTestApplication.context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
