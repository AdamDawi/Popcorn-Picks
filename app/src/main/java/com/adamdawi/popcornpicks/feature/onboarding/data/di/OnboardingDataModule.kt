package com.adamdawi.popcornpicks.feature.onboarding.data.di

import com.adamdawi.popcornpicks.feature.onboarding.data.repository.GenresRepositoryImpl
import com.adamdawi.popcornpicks.feature.onboarding.data.repository.MoviesByGenreRepositoryImpl
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.GenresRepository
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.MoviesByGenreRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val onboardingDataModule = module {
    singleOf(::MoviesByGenreRepositoryImpl).bind(MoviesByGenreRepository::class)
    singleOf(::GenresRepositoryImpl).bind(GenresRepository::class)
}