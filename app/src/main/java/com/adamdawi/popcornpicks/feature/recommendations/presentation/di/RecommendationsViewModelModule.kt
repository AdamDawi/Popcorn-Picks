package com.adamdawi.popcornpicks.feature.recommendations.presentation.di

import com.adamdawi.popcornpicks.feature.recommendations.presentation.RecommendationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val recommendationsViewModelModule = module{
    viewModelOf(::RecommendationsViewModel)
}