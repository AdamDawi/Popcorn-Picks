package com.adamdawi.popcornpicks.core.data.di

import android.content.SharedPreferences
import androidx.room.Room
import com.adamdawi.popcornpicks.core.data.local.GenresPreferencesImpl
import com.adamdawi.popcornpicks.core.data.local.MoviesDao
import com.adamdawi.popcornpicks.core.data.local.MoviesDbRepositoryImpl
import com.adamdawi.popcornpicks.core.data.local.OnBoardingManagerImpl
import com.adamdawi.popcornpicks.core.data.local.PopcornPicksDatabase
import com.adamdawi.popcornpicks.core.data.networking.HttpClientFactory
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.OnBoardingManager
import com.adamdawi.popcornpicks.core.domain.repository.MoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreDataModule = module {
    single<HttpClient> { HttpClientFactory().build() }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            Constants.Local.SHARED_PREFERENCES_NAME,
            android.content.Context.MODE_PRIVATE
        )
    }
    singleOf(::OnBoardingManagerImpl) { bind<OnBoardingManager>() }
    singleOf(::GenresPreferencesImpl) { bind<GenresPreferences>() }


    single<PopcornPicksDatabase>{
        Room.databaseBuilder(
            androidContext(),
            PopcornPicksDatabase::class.java,
            Constants.Database.DB_NAME
        ).build()
    }
    single<MoviesDao>{
        get<PopcornPicksDatabase>().moviesDao
    }
    singleOf(::MoviesDbRepositoryImpl) { bind<MoviesDbRepository>() }
}