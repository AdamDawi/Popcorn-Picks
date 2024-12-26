package com.adamdawi.popcornpicks.feature.onboarding.presentation.di

import com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen.GenresViewModel
import com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.MovieChooseViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val onboardingViewModelModule = module{
    viewModelOf(::MovieChooseViewModel)
    viewModelOf(::GenresViewModel)
}