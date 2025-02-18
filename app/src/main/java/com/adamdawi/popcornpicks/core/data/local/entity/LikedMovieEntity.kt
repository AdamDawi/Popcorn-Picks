package com.adamdawi.popcornpicks.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adamdawi.popcornpicks.core.data.mapper.mapGenreIdsToGenre
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.util.Constants.Database.LIKED_MOVIES_TABLE

@Entity(tableName = LIKED_MOVIES_TABLE)
data class LikedMovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val genresIds: List<Int>,
    val nextPage: Int = 1
)

fun LikedMovieEntity.toLikedMovie(): LikedMovie {
    return LikedMovie(
        id = id,
        title = title,
        poster = poster,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        genres = mapGenreIdsToGenre(genresIds),
        nextPage = nextPage
    )
}