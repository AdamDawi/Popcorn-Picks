package com.adamdawi.popcornpicks.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber
import com.adamdawi.popcornpicks.BuildConfig
import com.adamdawi.popcornpicks.core.data.di.coreDataModule
import com.adamdawi.popcornpicks.feature.genres_choose.data.di.genresModule
import com.adamdawi.popcornpicks.feature.movie_choose.data.di.movieChooseModule
import com.adamdawi.popcornpicks.feature.recommendations.data.di.recommendationsModule

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                coreDataModule,
                genresModule,
                movieChooseModule,
                recommendationsModule
            )
        }

    }
}