package com.adamdawi.popcornpicks.feature.user_profile.presentation.di

import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val profileViewModelModule = module{
    viewModelOf(::ProfileViewModel)
}