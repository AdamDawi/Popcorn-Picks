package com.adamdawi.popcornpicks.feature.recommendations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.MoviesByGenreRepository
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.RecommendationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecommendationsViewModel(
    private val moviesByGenreRepository: MoviesByGenreRepository,
    private val recommendationsRepository: RecommendationsRepository
): ViewModel() {
    private val _state = MutableStateFlow(RecommendationsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val res = recommendationsRepository.getMoviesBasedOnMovie(32134, 1)
            when(res){
                is Result.Error -> TODO()
                is Result.Success -> _state.update {
                    it.copy(recommendedMovies = res.data, recommendedMovie = res.data[0])
                }
            }
        }
    }

    fun onAction(action: RecommendationsAction) {
        when (action) {
            is RecommendationsAction.OnImageScratched -> setIsMovieScratchedStateToTrue()
            is RecommendationsAction.OnHeartClicked -> onHeartClicked()
            is RecommendationsAction.OnRerollClicked -> onRerollClicked()
            else -> {}
        }
    }

    private fun setIsMovieScratchedStateToTrue() {
        _state.value = _state.value.copy(isMovieScratched = true)
    }

    private fun onHeartClicked() {
        _state.value = _state.value.copy(isMovieLiked = !_state.value.isMovieLiked)
    }

    private fun onRerollClicked() {
        _state.value = _state.value.copy(
            isMovieLiked = false,
            isMovieScratched = false
        )
    }
}