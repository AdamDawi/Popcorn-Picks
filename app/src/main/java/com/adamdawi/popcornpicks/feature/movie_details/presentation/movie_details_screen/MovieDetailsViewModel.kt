package com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.util.Constants.NavArguments.MOVIE_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class MovieDetailsViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _state = MutableStateFlow(MovieDetailsState())
    val state = _state.onStart {
        getMovieId()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        MovieDetailsState()
    )
    private var movieId: String? = null

    private fun getMovieId(){
        movieId = savedStateHandle[MOVIE_ID]
        Timber.e(movieId)
    }

    fun onAction(action: MovieDetailsAction){
        when(action){
            else -> Unit
        }
    }
}