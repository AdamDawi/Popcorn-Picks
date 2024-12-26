package com.adamdawi.popcornpicks.feature.onboarding.data.di

import com.adamdawi.popcornpicks.feature.onboarding.data.repository.GenresRepositoryImpl
import com.adamdawi.popcornpicks.feature.onboarding.data.repository.MovieChooseRepositoryImpl
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.GenresRepository
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.MovieChooseRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val onboardingDataModule = module {
    singleOf(::MovieChooseRepositoryImpl).bind(MovieChooseRepository::class)
    singleOf(::GenresRepositoryImpl).bind(GenresRepository::class)
}