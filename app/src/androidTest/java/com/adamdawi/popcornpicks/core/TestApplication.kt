package com.adamdawi.popcornpicks.core

import android.app.Application
import org.koin.core.context.GlobalContext.startKoin

class TestApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules()
        }
    }
}