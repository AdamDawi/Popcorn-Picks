package com.adamdawi.popcornpicks.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber
import com.adamdawi.popcornpicks.BuildConfig
import com.adamdawi.popcornpicks.core.data.di.coreDataModule
import com.adamdawi.popcornpicks.feature.onboarding.data.di.onboardingDataModule
import com.adamdawi.popcornpicks.feature.onboarding.presentation.di.onboardingViewModelModule
import com.adamdawi.popcornpicks.feature.movie_details.data.di.movieDetailsDataModule
import com.adamdawi.popcornpicks.feature.movie_details.presentation.di.movieDetailsViewModelModule
import com.adamdawi.popcornpicks.feature.recommendations.data.di.recommendationsDataModule
import com.adamdawi.popcornpicks.feature.recommendations.presentation.di.recommendationsViewModelModule

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                coreDataModule,
                onboardingDataModule,
                onboardingViewModelModule,
                recommendationsDataModule,
                recommendationsViewModelModule,
                movieDetailsDataModule,
                movieDetailsViewModelModule
            )
        }

    }
}