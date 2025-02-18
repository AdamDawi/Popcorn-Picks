package com.adamdawi.popcornpicks.feature.recommendations.data.di

import com.adamdawi.popcornpicks.core.data.local.db.PopcornPicksDatabase
import com.adamdawi.popcornpicks.feature.recommendations.data.local.LocalMovieRecommendationsRepositoryImpl
import com.adamdawi.popcornpicks.feature.recommendations.data.local.dao.MovieRecommendationsDao
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.LocalMovieRecommendationsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recommendationsDataModule = module {
    singleOf(::LocalMovieRecommendationsRepositoryImpl).bind(LocalMovieRecommendationsRepository::class)
    single<MovieRecommendationsDao>{
        get<PopcornPicksDatabase>().movieRecommendationsDao
    }
}