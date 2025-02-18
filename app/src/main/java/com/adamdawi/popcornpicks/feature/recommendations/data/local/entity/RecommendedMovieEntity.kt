package com.adamdawi.popcornpicks.feature.recommendations.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adamdawi.popcornpicks.core.data.mapper.mapGenreIdsToGenre
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.util.Constants.Database.RECOMMENDED_MOVIES_TABLE

@Entity(tableName = RECOMMENDED_MOVIES_TABLE)
data class RecommendedMovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val genresIds: List<Int>
)

fun RecommendedMovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        poster = poster,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        genres = mapGenreIdsToGenre(genresIds)
    )
}