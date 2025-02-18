package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.core.domain.local.GenresRepository
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.LocalMovieRecommendationsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecommendationsViewModel(
    private val genresRepository: GenresRepository,
    private val remoteMovieRecommendationsRepository: RemoteMovieRecommendationsRepository,
    private val localMovieRecommendationsRepository: LocalMovieRecommendationsRepository,
    private val likedMoviesDbRepository: LikedMoviesDbRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(RecommendationsState())
    val state = _state.onStart {
        getRecommendedMoviesFromDb()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

    private val eventChannel = Channel<RecommendationsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var likedMoviesList: MutableList<LikedMovie> = mutableListOf()
    private var currentRecommendedMovieIndex = 0

    private fun getRecommendedMoviesFromDb(){
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch(ioDispatcher) {
            val result = localMovieRecommendationsRepository.getRecommendedMovies()
            when(result){
                is Result.Error -> {
                    fetchRecommendedMovies()
                }
                is Result.Success -> {
                    if(result.data.isEmpty()){
                        fetchRecommendedMovies()
                    }else{
                        _state.update {
                            it.copy(
                                recommendedMovies = result.data,
                                recommendedMovie = result.data[currentRecommendedMovieIndex],
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private suspend fun fetchRecommendedMovies() {
        if(likedMoviesList.isEmpty()){
            val result = likedMoviesDbRepository.getLikedMovies()
            when (result) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
                is Result.Success -> {
                    likedMoviesList = result.data.toMutableList()
                    val likedMovie = likedMoviesList.find {
                        it.nextPage == 1
                    } ?: likedMoviesList.find {
                        it.nextPage == 2
                    } ?: LikedMovie(0, "", "", "", 0.0, listOf(), 0) //TODO fetch from movies based on genres

                    fetchRecommendedMoviesFromApi(likedMovie.id, likedMovie.nextPage)
                }
            }
        }else{
            val likedMovie = likedMoviesList.find {
                it.nextPage == 1
            } ?: likedMoviesList.find {
                it.nextPage == 2
            } ?: LikedMovie(0, "", "", "", 0.0, listOf(), 0) //TODO fetch from movies based on genres

            fetchRecommendedMoviesFromApi(likedMovie.id, likedMovie.nextPage)
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
                        recommendedMovie = result.data[currentRecommendedMovieIndex],
                        isLoading = false
                    )
                }
                localMovieRecommendationsRepository.addRecommendedMovies(result.data)
                likedMoviesDbRepository.updatePageForLikedMovie(movieId, page+1)
                val index = likedMoviesList.indexOfFirst { it.id == movieId }
                if (index != -1) {
                    likedMoviesList[index] = likedMoviesList[index].copy(nextPage = page + 1)
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
        _state.update {
            it.copy(
                isMovieLiked = false,
                isMovieScratched = false
            )
        }
        viewModelScope.launch {
            delay(200)
            if (currentRecommendedMovieIndex < _state.value.recommendedMovies.size - 1) {
                //Getting a new movie
                currentRecommendedMovieIndex++
                _state.update {
                    it.copy(recommendedMovie = _state.value.recommendedMovies[currentRecommendedMovieIndex])
                }

                // Deleting the previous movie (if there was one)
                if (currentRecommendedMovieIndex > 0) {
                    localMovieRecommendationsRepository.deleteRecommendedMovie(_state.value.recommendedMovies[currentRecommendedMovieIndex - 1])
                }

            } else {
                //If the end of the list has been reached
                localMovieRecommendationsRepository.deleteRecommendedMovie(_state.value.recommendedMovies[currentRecommendedMovieIndex])
                currentRecommendedMovieIndex = 0
                fetchRecommendedMovies()
            }
        }
    }
}