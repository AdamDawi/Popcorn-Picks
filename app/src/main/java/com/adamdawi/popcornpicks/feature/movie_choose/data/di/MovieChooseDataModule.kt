package com.adamdawi.popcornpicks.feature.movie_choose.data.di

import com.adamdawi.popcornpicks.feature.movie_choose.data.repository.MovieChooseRepositoryImpl
import com.adamdawi.popcornpicks.feature.movie_choose.domain.repository.MovieChooseRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val movieChooseDataModule = module {
    singleOf(::MovieChooseRepositoryImpl).bind(MovieChooseRepository::class)
}