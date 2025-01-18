package com.example.comicsappmobile

import android.app.Application
import com.example.comicsappmobile.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ComicsAppApplication: Application() {

    // Initialize Koin
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ComicsAppApplication )
            modules(appModule)
        }
    }
}