package com.adamdawi.popcornpicks.feature.recommendations.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adamdawi.popcornpicks.core.domain.util.Constants.Database.RECOMMENDED_MOVIES_TABLE
import com.adamdawi.popcornpicks.feature.recommendations.data.local.entity.RecommendedMovieEntity

@Dao
interface MovieRecommendationsDao {
    @Query("SELECT * FROM `$RECOMMENDED_MOVIES_TABLE`")
    fun getRecommendedMovies(): List<RecommendedMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRecommendedMovies(movies: List<RecommendedMovieEntity>)

    @Delete
    suspend fun deleteRecommendedMovie(movie: RecommendedMovieEntity)
}