package com.adamdawi.popcornpicks.feature.genres_choose.data.di

import com.adamdawi.popcornpicks.feature.genres_choose.data.repository.GenresRepositoryImpl
import com.adamdawi.popcornpicks.feature.genres_choose.domain.repository.GenresRepository
import com.adamdawi.popcornpicks.feature.genres_choose.presentation.GenresViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val genresModule = module {
    singleOf(::GenresRepositoryImpl).bind(GenresRepository::class)
    viewModelOf(::GenresViewModel)
}