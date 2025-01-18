package com.example.comicsappmobile.utils

import android.util.Log
import com.example.comicsappmobile.BuildConfig

object Logger {
    fun debug(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun error(tag: String, message: String, throwable: Throwable? = null) {
        Log.e(tag, message, throwable)
    }
}