package com.adamdawi.popcornpicks.feature.onboarding.data.di

import com.adamdawi.popcornpicks.feature.onboarding.data.remote.GenresRepositoryImpl
import com.adamdawi.popcornpicks.feature.onboarding.domain.remote.GenresRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val onboardingDataModule = module{
    singleOf(::GenresRepositoryImpl){ bind<GenresRepository>() }
}