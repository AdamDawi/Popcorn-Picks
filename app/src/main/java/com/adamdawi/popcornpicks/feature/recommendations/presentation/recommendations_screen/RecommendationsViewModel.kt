package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecommendationsViewModel(
    private val remoteMovieRecommendationsRepository: RemoteMovieRecommendationsRepository,
    private val likedMoviesDbRepository: LikedMoviesDbRepository,
    private val databaseDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(RecommendationsState())
    val state = _state.onStart {
        fetchRecommendedMovies()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

    private val eventChannel = Channel<RecommendationsEvent>()
    val events = eventChannel.receiveAsFlow()

    private fun fetchRecommendedMovies() {
        viewModelScope.launch(databaseDispatcher) {
            _state.value = _state.value.copy(isLoading = true)
            val result = likedMoviesDbRepository.getLikedMovies()
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
        val result = remoteMovieRecommendationsRepository.getMoviesBasedOnMovie(movieId, page)
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
                        recommendedMovie = result.data[0],
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