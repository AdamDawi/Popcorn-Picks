package com.adamdawi.popcornpicks.feature.genres_choose.data.di

import com.adamdawi.popcornpicks.feature.genres_choose.data.repository.GenresRepositoryImpl
import com.adamdawi.popcornpicks.feature.genres_choose.domain.repository.GenresRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val genresDataModule = module {
    singleOf(::GenresRepositoryImpl).bind(GenresRepository::class)
}