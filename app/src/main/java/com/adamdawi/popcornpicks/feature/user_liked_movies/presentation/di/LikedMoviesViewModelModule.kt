package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.di

import com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen.LikedMoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val likedMoviesViewModelModule = module{
    viewModelOf(::LikedMoviesViewModel)
}