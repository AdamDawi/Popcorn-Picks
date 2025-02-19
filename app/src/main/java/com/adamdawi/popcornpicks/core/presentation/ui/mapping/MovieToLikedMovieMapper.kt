package com.adamdawi.popcornpicks.core.presentation.ui.mapping

import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.model.Movie

fun Movie.toLikedMovie(): LikedMovie{
    return LikedMovie(
        id = id,
        title = title,
        poster = poster,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        genres = genres,
        nextPage = 1
    )
}