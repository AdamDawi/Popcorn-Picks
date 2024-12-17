package com.adamdawi.popcornpicks.feature.movie_choose.data.di

import com.adamdawi.popcornpicks.feature.movie_choose.data.repository.MovieChooseRepositoryImpl
import com.adamdawi.popcornpicks.feature.movie_choose.domain.repository.MovieChooseRepository
import com.adamdawi.popcornpicks.feature.movie_choose.presentation.MovieChooseViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val movieChooseModule = module {
    singleOf(::MovieChooseRepositoryImpl).bind(MovieChooseRepository::class)
    viewModelOf(::MovieChooseViewModel)
}