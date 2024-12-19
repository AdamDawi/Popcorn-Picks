package com.adamdawi.popcornpicks.feature.genres_choose.presentation.di

import com.adamdawi.popcornpicks.feature.genres_choose.presentation.GenresViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val genresViewModelModule = module{
    viewModelOf(::GenresViewModel)
}