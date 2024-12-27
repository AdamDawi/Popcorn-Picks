package com.adamdawi.popcornpicks.feature.recommendations.data.di

import com.adamdawi.popcornpicks.feature.recommendations.data.repository.RecommendationsRepositoryImpl
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.RecommendationsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val recommendationsDataModule = module {
    singleOf(::RecommendationsRepositoryImpl).bind(RecommendationsRepository::class)
}