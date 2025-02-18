package com.adamdawi.popcornpicks.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adamdawi.popcornpicks.core.data.local.dao.LikedMoviesDao
import com.adamdawi.popcornpicks.core.data.local.entity.LikedMovieEntity
import com.adamdawi.popcornpicks.feature.recommendations.data.local.dao.MovieRecommendationsDao
import com.adamdawi.popcornpicks.feature.recommendations.data.local.entity.RecommendedMovieEntity

@Database(
    entities = [
        LikedMovieEntity::class, RecommendedMovieEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PopcornPicksDatabase : RoomDatabase() {
    abstract val likedMoviesDao: LikedMoviesDao
    abstract val movieRecommendationsDao: MovieRecommendationsDao
}