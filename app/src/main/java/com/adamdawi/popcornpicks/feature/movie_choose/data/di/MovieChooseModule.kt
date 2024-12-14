package com.adamdawi.popcornpicks.feature.movie_choose.data.di

import com.adamdawi.popcornpicks.feature.movie_choose.presentation.MovieChooseViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val movieChooseModule = module {
    viewModelOf(::MovieChooseViewModel)
}