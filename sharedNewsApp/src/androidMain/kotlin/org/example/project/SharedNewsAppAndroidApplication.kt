package org.example.project

import android.app.Application
import org.example.project.core.network.di.networkModule
import org.example.project.features.headlines.di.headlinesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SharedNewsAppAndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SharedNewsAppAndroidApplication)
            modules(networkModule)
            modules(headlinesModule)
        }
    }
}