package com.adamdawi.popcornpicks.e2e

import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.adamdawi.popcornpicks.core.data.local.GenresPreferencesImpl
import com.adamdawi.popcornpicks.core.data.local.LikedMoviesDbRepositoryImpl
import com.adamdawi.popcornpicks.core.data.local.OnBoardingManagerImpl
import com.adamdawi.popcornpicks.core.data.local.dao.LikedMoviesDao
import com.adamdawi.popcornpicks.core.data.local.db.PopcornPicksDatabase
import com.adamdawi.popcornpicks.core.data.networking.HttpClientFactory
import com.adamdawi.popcornpicks.core.data.remote.RemoteMovieRecommendationsRepositoryImpl
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.local.OnBoardingManager
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.e2e.response.generateMockGenresResponse
import com.adamdawi.popcornpicks.e2e.response.generateMockMoviesBasedOnGenreResponse
import com.adamdawi.popcornpicks.e2e.response.generateMockMoviesBasedOnMovieResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val testCoreDataModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            Constants.Local.SHARED_PREFERENCES_NAME,
            android.content.Context.MODE_PRIVATE
        )
    }
    singleOf(::OnBoardingManagerImpl) { bind<OnBoardingManager>() }
    singleOf(::GenresPreferencesImpl) { bind<GenresPreferences>() }

    single<LikedMoviesDao>{
        get<PopcornPicksDatabase>().likedMoviesDao
    }
    singleOf(::LikedMoviesDbRepositoryImpl) { bind<LikedMoviesDbRepository>() }
    singleOf(::RemoteMovieRecommendationsRepositoryImpl) { bind<RemoteMovieRecommendationsRepository>() }

    single {
        // In-memory Room DB
        Room.inMemoryDatabaseBuilder(
            androidContext(),
            PopcornPicksDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    single<HttpClient> { HttpClientFactory.build(
        MockEngine.create {
            addHandler {
                    request ->
                val endpoint = request.url.encodedPath
                Log.e("endpoint", endpoint)

                val mockResponseContent = when {
                    endpoint.contains("/genre/movie/list") -> generateMockGenresResponse()
                    endpoint.contains("/discover/movie") -> generateMockMoviesBasedOnGenreResponse()
                    endpoint.contains("recommendations") -> generateMockMoviesBasedOnMovieResponse()
                    else -> ""
                }

                respond(
                    content = mockResponseContent,
                    status = HttpStatusCode.OK,
                    headers = headersOf("Content-Type" to listOf("application/json"))
                )
            }
        }
    )}
}