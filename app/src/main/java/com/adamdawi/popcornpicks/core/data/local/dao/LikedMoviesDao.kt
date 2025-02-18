package com.adamdawi.popcornpicks.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adamdawi.popcornpicks.core.data.local.entity.LikedMovieEntity
import com.adamdawi.popcornpicks.core.domain.util.Constants.Database.LIKED_MOVIES_TABLE

@Dao
interface LikedMoviesDao {
    @Query("SELECT * FROM `$LIKED_MOVIES_TABLE`")
    fun getLikedMovies(): List<LikedMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLikedMovies(movies: List<LikedMovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLikedMovie(movie: LikedMovieEntity)

    @Query("UPDATE `$LIKED_MOVIES_TABLE` SET nextPage = :nextPage WHERE id = :movieId")
    suspend fun updatePageForLikedMovie(movieId: Int, nextPage: Int)

    @Delete
    suspend fun deleteLikedMovie(movie: LikedMovieEntity)
}