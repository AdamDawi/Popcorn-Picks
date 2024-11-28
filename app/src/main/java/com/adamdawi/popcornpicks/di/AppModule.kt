package com.adamdawi.popcornpicks.di

import com.adamdawi.popcornpicks.feature.genres_choose.presentation.GenresViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
//    single<PatternValidator>{
//        EmailPatternValidator
//    }
//
//    singleOf(::UserDataValidator)

    viewModelOf(::GenresViewModel)
}