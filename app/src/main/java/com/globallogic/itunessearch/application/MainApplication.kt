package com.globallogic.itunessearch.application

import android.app.Application
import com.globallogic.data.di.networkModule
import com.globallogic.itunessearch.di.baseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
            modules(listOf(networkModule, baseModule))
        }
    }
}
