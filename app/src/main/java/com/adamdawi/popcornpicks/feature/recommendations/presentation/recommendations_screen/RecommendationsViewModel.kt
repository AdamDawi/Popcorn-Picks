package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.toLikedMovie
import com.adamdawi.popcornpicks.feature.recommendations.domain.repository.LocalMovieRecommendationsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
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
    genresPreferences: GenresPreferences,
    private val remoteMovieRecommendationsRepository: RemoteMovieRecommendationsRepository,
    private val localMovieRecommendationsRepository: LocalMovieRecommendationsRepository,
    private val likedMoviesDbRepository: LikedMoviesDbRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(RecommendationsState())
    val state = _state.onStart {
        loadCachedRecommendations()
        loadLikedMovies()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

    private val eventChannel = Channel<RecommendationsEvent>()
    val events = eventChannel.receiveAsFlow()

    private var loadingLikedMoviesJob: Job = Job()
    private var likedMoviesMap: MutableMap<Int, LikedMovie> = mutableMapOf()
    private var recommendedMoviesList: MutableList<Movie> = mutableListOf()
    private val likedGenres = genresPreferences.getGenres()
    private val currentLikedGenreIndex = 0
    private var currentRecommendedMovieIndex = 0

    private fun loadCachedRecommendations(){
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        viewModelScope.launch(ioDispatcher) {
            val result = localMovieRecommendationsRepository.getRecommendedMovies()
            when(result){
                is Result.Error -> {
                    fetchNewRecommendations()
                }
                is Result.Success -> {
                    if(result.data.isEmpty()){
                        fetchNewRecommendations()
                    }else{
                        _state.update {
                            it.copy(
                                recommendedMovie = result.data[currentRecommendedMovieIndex],
                                isLoading = false
                            )
                        }
                        recommendedMoviesList = result.data.toMutableList()
                    }
                }
            }
        }
    }

    private suspend fun fetchNewRecommendations() {
        if(likedMoviesMap.isEmpty() && loadingLikedMoviesJob.isActive){
            loadingLikedMoviesJob.join()
        }

        val movieForRecommendations = likedMoviesMap.values.firstOrNull { it.nextPage == 1 }
            ?: likedMoviesMap.values.firstOrNull { it.nextPage == 2 }

        if (movieForRecommendations != null) {
            fetchRecommendedMoviesFromApiByMovie(movieForRecommendations.id, movieForRecommendations.nextPage)
        } else {
            fetchRecommendedMoviesFromApiByGenre(likedGenres[0], 0) //TODO change id and page
        }
    }

    private suspend fun fetchRecommendedMoviesFromApiByMovie(movieId: Int, page: Int) {
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
                        recommendedMovie = result.data[currentRecommendedMovieIndex],
                        isLoading = false
                    )
                }
                recommendedMoviesList = result.data.filter { item ->
                    item.id !in likedMoviesMap.keys
                }.toMutableList()
                localMovieRecommendationsRepository.addRecommendedMovies(result.data)
                updateLikedMoviePage(movieId, page)
            }
        }
    }

    private suspend fun updateLikedMoviePage(movieId: Int, page: Int) {
        likedMoviesDbRepository.updatePageForLikedMovie(movieId, page+1)
        likedMoviesMap[movieId]?.let {
            likedMoviesMap[movieId] = it.copy(nextPage = page + 1)
        }
    }

    private suspend fun fetchRecommendedMoviesFromApiByGenre(genreId: String, page: Int) {
        val result = remoteMovieRecommendationsRepository.getMoviesBasedOnGenre(genreId, page)
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
                        recommendedMovie = result.data[currentRecommendedMovieIndex],
                        isLoading = false
                    )
                }
                recommendedMoviesList = result.data.filter { item ->
                    item.id !in likedMoviesMap.keys
                }.toMutableList()
                localMovieRecommendationsRepository.addRecommendedMovies(result.data)
                //TODO update genre page
            }
        }
    }

    private fun loadLikedMovies(){
        if(likedMoviesMap.isEmpty()){
            loadingLikedMoviesJob.cancel()
            loadingLikedMoviesJob = viewModelScope.launch(ioDispatcher) {
                val result = likedMoviesDbRepository.getLikedMovies()
                when (result) {
                    is Result.Error -> {
                        _state.update {
                            it.copy(
                                error = result.error.asUiText()
                            )
                        }
                    }
                    is Result.Success -> {
                        likedMoviesMap = result.data.associateBy { it.id }.toMutableMap()
                    }
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
        viewModelScope.launch(ioDispatcher) {
            if(_state.value.isMovieLiked){
                val result = likedMoviesDbRepository.addLikedMovie(_state.value.recommendedMovie)
                when(result){
                    is Result.Error -> {
                        eventChannel.send(RecommendationsEvent.Error(result.error.asUiText()))
                    }
                    is Result.Success -> {
                        likedMoviesMap[_state.value.recommendedMovie.id] = _state.value.recommendedMovie.toLikedMovie()

                    }
                }
            }else{
                val result = likedMoviesDbRepository.deleteLikedMovie(_state.value.recommendedMovie)
                when(result){
                    is Result.Error -> {
                        eventChannel.send(RecommendationsEvent.Error(result.error.asUiText()))
                    }
                    is Result.Success -> {
                        likedMoviesMap.remove(_state.value.recommendedMovie.id)
                    }
                }
            }
        }
    }

    private fun onRerollClicked() {
        _state.update {
            it.copy(
                isMovieLiked = false,
                isMovieScratched = false
            )
        }
        viewModelScope.launch(ioDispatcher) {
            delay(200)
            if (currentRecommendedMovieIndex < recommendedMoviesList.size - 1) {
                //Getting a new movie
                currentRecommendedMovieIndex++
                _state.update {
                    it.copy(recommendedMovie = recommendedMoviesList[currentRecommendedMovieIndex])
                }

                // Deleting the previous movie (if there was one)
                if (currentRecommendedMovieIndex > 0) {
                    localMovieRecommendationsRepository.deleteRecommendedMovie(recommendedMoviesList[currentRecommendedMovieIndex - 1])
                    recommendedMoviesList.removeAt(currentRecommendedMovieIndex - 1)
                }

            } else {
                //If the end of the list has been reached
                localMovieRecommendationsRepository.deleteRecommendedMovie(recommendedMoviesList[currentRecommendedMovieIndex])
                currentRecommendedMovieIndex = 0
                recommendedMoviesList.clear()
                fetchNewRecommendations()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadingLikedMoviesJob.cancel()
    }
}