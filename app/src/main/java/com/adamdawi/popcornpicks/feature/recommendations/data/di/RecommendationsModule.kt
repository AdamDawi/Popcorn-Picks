package com.adamdawi.popcornpicks.feature.recommendations.data.di

import com.adamdawi.popcornpicks.feature.recommendations.presentation.RecommendationsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val recommendationsModule = module {
    viewModelOf(::RecommendationsViewModel)
}