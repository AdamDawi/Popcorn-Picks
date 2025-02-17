package com.adamdawi.popcornpicks.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adamdawi.popcornpicks.core.data.local.entity.LikedMovieEntity
import com.adamdawi.popcornpicks.core.domain.util.Constants.Database.MOVIES_TABLE

@Dao
interface LikedMoviesDao {
    @Query("SELECT * FROM `$MOVIES_TABLE`")
    fun getLikedMovies(): List<LikedMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLikedMovies(movies: List<LikedMovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLikedMovie(movie: LikedMovieEntity)

    @Delete
    suspend fun deleteLikedMovie(movie: LikedMovieEntity)
}