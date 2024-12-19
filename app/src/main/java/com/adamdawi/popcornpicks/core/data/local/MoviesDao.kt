package com.adamdawi.popcornpicks.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adamdawi.popcornpicks.core.domain.model.MovieEntity
import com.adamdawi.popcornpicks.core.domain.util.Constants.Database.MOVIES_TABLE

@Dao
interface MoviesDao {
    @Query("SELECT * FROM `$MOVIES_TABLE`")
    fun getMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)
}