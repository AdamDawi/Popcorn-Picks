package com.adamdawi.popcornpicks.feature.movie_details.data.di

import com.adamdawi.popcornpicks.feature.movie_details.presentation.MovieDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val movieDetailsModule = module{
    viewModelOf(::MovieDetailsViewModel)
}