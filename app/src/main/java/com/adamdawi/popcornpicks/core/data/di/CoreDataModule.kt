package com.adamdawi.popcornpicks.core.data.di

import android.content.SharedPreferences
import com.adamdawi.popcornpicks.core.data.local.OnBoardingManagerImpl
import com.adamdawi.popcornpicks.core.data.networking.HttpClientFactory
import com.adamdawi.popcornpicks.core.domain.OnBoardingManager
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreDataModule = module {
    single<HttpClient> { HttpClientFactory().build() }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "popcorn_picks_preferences",
            android.content.Context.MODE_PRIVATE
        )
    }
    singleOf(::OnBoardingManagerImpl) { bind<OnBoardingManager>() }
}