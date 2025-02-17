package com.adamdawi.popcornpicks.feature.recommendations.presentation.di

import com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen.RecommendationsViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val recommendationsViewModelModule = module{
    single<CoroutineDispatcher> { Dispatchers.IO }
    viewModelOf(::RecommendationsViewModel)
}