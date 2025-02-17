package com.adamdawi.popcornpicks.feature.recommendations.data.di

import com.adamdawi.popcornpicks.core.data.remote.RemoteMovieRecommendationsRepositoryImpl
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recommendationsDataModule = module {
    singleOf(::RemoteMovieRecommendationsRepositoryImpl).bind(RemoteMovieRecommendationsRepository::class)
}