package com.adamdawi.popcornpicks.feature.movie_details.data.di

import com.adamdawi.popcornpicks.feature.movie_details.data.remote.MovieDetailsRepositoryImpl
import com.adamdawi.popcornpicks.feature.movie_details.domain.remote.MovieDetailsRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val movieDetailsDataModule = module{
    singleOf(::MovieDetailsRepositoryImpl){ bind<MovieDetailsRepository>() }
}