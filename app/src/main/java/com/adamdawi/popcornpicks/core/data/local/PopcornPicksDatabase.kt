package com.adamdawi.popcornpicks.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adamdawi.popcornpicks.core.domain.model.MovieEntity

@Database(
    entities = [
        MovieEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class PopcornPicksDatabase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
}