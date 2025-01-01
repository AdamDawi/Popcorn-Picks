package com.adamdawi.popcornpicks.feature.recommendations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.repository.MoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.MoviesByGenreRepository
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.RecommendationsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecommendationsViewModel(
    private val moviesByGenreRepository: MoviesByGenreRepository,
    private val recommendationsRepository: RecommendationsRepository,
    private val moviesDbRepository: MoviesDbRepository,
    private val databaseDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _state = MutableStateFlow(RecommendationsState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<RecommendationsEvent>()
    val events = eventChannel.receiveAsFlow()


    init {
        fetchRecommendedMovies()
    }

    private fun fetchRecommendedMovies() {
        viewModelScope.launch(databaseDispatcher) {
            _state.value = _state.value.copy(isLoading = true)
            val result = moviesDbRepository.getMovies()
            when (result) {
                is Result.Error -> {}
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            likedMovies = result.data
                        )
                    }
                    fetchRecommendedMoviesFromApi(_state.value.likedMovies[0].id, 1)
                }
            }
        }
    }

    private suspend fun fetchRecommendedMoviesFromApi(movieId: Int, page: Int) {
        val result = recommendationsRepository.getMoviesBasedOnMovie(movieId, page)
        when (result) {
            is Result.Error -> {
                _state.update {
                    it.copy(
                        error = result.error.asUiText(),
                        isLoading = false
                    )
                }
            }

            is Result.Success -> {
                _state.update {
                    it.copy(
                        recommendedMovies = result.data,
//                        recommendedMovie = result.data[0],
                        isLoading = false
                    )
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