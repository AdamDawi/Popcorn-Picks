package com.adamdawi.popcornpicks.core.data.mapper

import com.adamdawi.popcornpicks.core.data.local.entity.LikedMovieEntity
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.feature.recommendations.data.local.entity.RecommendedMovieEntity


fun Movie.toLikedMovieEntity(): LikedMovieEntity {
    return LikedMovieEntity(
        id = id,
        title = title,
        poster = poster,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        genresIds = genres.map { it.id }
    )
}

fun Movie.toRecommendedMovieEntity(): RecommendedMovieEntity{
    return RecommendedMovieEntity(
        id = id,
        title = title,
        poster = poster,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        genresIds = genres.map { it.id }
    )
}