package com.adamdawi.popcornpicks.feature.recommendations.presentation

import androidx.lifecycle.ViewModel
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecommendationsViewModel: ViewModel() {
    private val _state = MutableStateFlow(RecommendationsState(Movie("", "", "")))
    val state = _state.asStateFlow()

    fun onAction(action: RecommendationsAction) {
        when (action) {
            is RecommendationsAction.OnImageScratched -> onImageScratched()
            else -> {}
        }
    }

    fun onImageScratched() {
        _state.value = _state.value.copy(isMovieScratched = true)
    }
}