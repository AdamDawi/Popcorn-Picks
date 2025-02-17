package com.adamdawi.popcornpicks.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adamdawi.popcornpicks.core.data.local.LikedMoviesDao
import com.adamdawi.popcornpicks.core.data.local.entity.LikedMovieEntity

@Database(
    entities = [
        LikedMovieEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PopcornPicksDatabase : RoomDatabase() {
    abstract val likedMoviesDao: LikedMoviesDao
}