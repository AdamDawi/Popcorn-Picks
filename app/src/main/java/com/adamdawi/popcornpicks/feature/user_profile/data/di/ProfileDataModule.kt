package com.adamdawi.popcornpicks.feature.user_profile.data.di

import com.adamdawi.popcornpicks.feature.user_profile.data.local.UserProfilePreferencesImpl
import com.adamdawi.popcornpicks.feature.user_profile.domain.local.UserProfilePreferences
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val profileDataModule = module{
    singleOf(::UserProfilePreferencesImpl) { bind<UserProfilePreferences>() }

}