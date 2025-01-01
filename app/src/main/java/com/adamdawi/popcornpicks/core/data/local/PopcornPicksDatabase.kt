package com.adamdawi.popcornpicks.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adamdawi.popcornpicks.core.domain.model.MovieEntity

@Database(
    entities = [
        MovieEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PopcornPicksDatabase : RoomDatabase() {
    abstract val moviesDao: MoviesDao
}