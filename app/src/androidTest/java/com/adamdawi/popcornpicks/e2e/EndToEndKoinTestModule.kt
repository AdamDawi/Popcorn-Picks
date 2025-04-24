package com.adamdawi.popcornpicks.e2e

import android.content.SharedPreferences
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
import com.adamdawi.popcornpicks.e2e.response.generateMockDetailedMovieId1Response
import com.adamdawi.popcornpicks.e2e.response.generateMockDetailedMovieResponse
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
        Room.inMemoryDatabaseBuilder(
            androidContext(),
            PopcornPicksDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    single<HttpClient> { HttpClientFactory.build(
        MockEngine.create {
            addHandler { request ->
                val endpoint = request.url.encodedPath

                val mockResponse = when {
                    endpoint.contains("/genre/movie/list") -> generateMockGenresResponse() to HttpStatusCode.OK
                    endpoint.contains("/discover/movie") -> generateMockMoviesBasedOnGenreResponse() to HttpStatusCode.OK
                    endpoint.contains("recommendations") -> generateMockMoviesBasedOnMovieResponse() to HttpStatusCode.OK
                    endpoint.contains("/movie/1") -> generateMockDetailedMovieId1Response() to HttpStatusCode.OK // movie details for movieId = 1
                    endpoint.contains("/movie") -> generateMockDetailedMovieResponse() to HttpStatusCode.OK // movie details for any movieId
                    else -> "Not mocked" to HttpStatusCode.NotFound
                }

                respond(
                    content = mockResponse.first,
                    status = mockResponse.second,
                    headers = headersOf("Content-Type" to listOf("application/json"))
                )
            }
        }
    )}
}