package com.adamdawi.popcornpicks.di

import com.adamdawi.popcornpicks.core.data.networking.HttpClientFactory
import com.adamdawi.popcornpicks.feature.genres_choose.data.repository.GenresRepositoryImpl
import com.adamdawi.popcornpicks.feature.genres_choose.domain.repository.GenresRepository
import com.adamdawi.popcornpicks.feature.genres_choose.presentation.GenresViewModel
import com.adamdawi.popcornpicks.feature.movie_choose.presentation.MovieChooseViewModel
import com.adamdawi.popcornpicks.feature.recommendations.presentation.RecommendationsViewModel
import io.ktor.client.HttpClient
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<HttpClient> { HttpClientFactory().build() }
    single<GenresRepository> { GenresRepositoryImpl(get()) }
    viewModelOf(::GenresViewModel)

    viewModelOf(::MovieChooseViewModel)
    viewModelOf(::RecommendationsViewModel)
}