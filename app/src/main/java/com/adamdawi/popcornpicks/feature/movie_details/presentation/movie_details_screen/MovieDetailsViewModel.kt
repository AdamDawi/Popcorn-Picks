package com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieDetailsViewModel: ViewModel() {
    private val _state = MutableStateFlow(MovieDetailsState())
    val state = _state.asStateFlow()

    fun onAction(action: MovieDetailsAction){
        when(action){
            else -> Unit
        }
    }
}