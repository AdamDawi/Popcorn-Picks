package com.adamdawi.popcornpicks.feature.movie_details.presentation.di

import com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen.MovieDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val movieDetailsViewModelModule = module{
    viewModelOf(::MovieDetailsViewModel)
}