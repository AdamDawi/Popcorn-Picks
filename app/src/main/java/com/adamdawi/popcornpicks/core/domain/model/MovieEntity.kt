package com.adamdawi.popcornpicks.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adamdawi.popcornpicks.core.domain.util.Constants.Database.MOVIES_TABLE
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie

@Entity(tableName = MOVIES_TABLE)
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster: String?,
    val releaseDate: String,
)

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        poster = poster,
        releaseDate = releaseDate
    )
}