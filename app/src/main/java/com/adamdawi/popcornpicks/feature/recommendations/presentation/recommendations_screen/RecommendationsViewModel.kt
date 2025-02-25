package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.remote.RemoteMovieRecommendationsRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants.SavedStateHandleArguments.IS_MOVIE_SCRATCHED
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
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
    private val savedStateHandle: SavedStateHandle,
    private val genresPreferences: GenresPreferences,
    private val remoteMovieRecommendationsRepository: RemoteMovieRecommendationsRepository,
    private val localMovieRecommendationsRepository: LocalMovieRecommendationsRepository,
    private val likedMoviesDbRepository: LikedMoviesDbRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(RecommendationsState(
        isMovieScratched = savedStateHandle.get<Boolean>(IS_MOVIE_SCRATCHED) == true
    ))
    val state = _state.onStart {
        loadCachedRecommendations()
        loadLikedMovies()
        loadLikedGenres()
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
    private val likedGenresWithPageMap = mutableMapOf<String, Int>()
    // Flag to prevent multiple rapid clicks on the reroll button,
    // ensuring that the reroll process is not triggered simultaneously multiple times.
    private var isProcessingOnReroll = false

    private fun loadCachedRecommendations() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch(ioDispatcher) {
            val result = localMovieRecommendationsRepository.getRecommendedMovies()

            if (result is Result.Success && result.data.isNotEmpty()) {
                recommendedMoviesList = result.data.toMutableList()
                _state.update {
                    it.copy(
                        recommendedMovie = recommendedMoviesList.first(),
                        isLoading = false
                    )
                }
            } else {
                fetchNewRecommendations()
            }
        }
    }

    private suspend fun fetchNewRecommendations() {
        waitForLikedMoviesIfLoading()
        if(likedMoviesMap.isEmpty() && likedGenresWithPageMap.isEmpty()){
            _state.update {
                it.copy(
                    error = "Something went wrong",
                    isLoading = false
                )
            }
            return
        }

        // 1. Try to get recommendations based on liked movies
        val movieForRecommendations = findMovieForRecommendations()
        if (movieForRecommendations != null) {
            fetchRecommendedMoviesFromApiByMovie(movieForRecommendations.id, movieForRecommendations.nextPage)
            return
        }

        // 2. If no movie-based recommendations are found, try based on liked genres
        val genreForRecommendations = findGenreForRecommendations()
        if (genreForRecommendations != null) {
            fetchRecommendedMoviesFromApiByGenre(genreForRecommendations.key, genreForRecommendations.value)
        }
        else {
            // 3. If no recommendations are available, reset pages and retry
            resetLikedMoviesPages()
            resetLikedGenresPages()
            fetchNewRecommendations()
        }
    }

    private suspend fun waitForLikedMoviesIfLoading() {
        if (likedMoviesMap.isEmpty() && loadingLikedMoviesJob.isActive) {
            loadingLikedMoviesJob.join()
        }
    }

    private fun findMovieForRecommendations(): LikedMovie? {
        return likedMoviesMap.values.firstOrNull { it.nextPage == 1 }
            ?: likedMoviesMap.values.firstOrNull { it.nextPage == 2 }
    }

    private fun findGenreForRecommendations(): Map.Entry<String, Int>? {
        return likedGenresWithPageMap.minByOrNull { it.value }
            ?.takeIf { it.value < 15 }
    }

    private suspend fun resetLikedMoviesPages() {
        likedMoviesMap.replaceAll { _, likedMovie -> likedMovie.copy(nextPage = 1) }
        likedMoviesDbRepository.updatePageForAllLikedMovies(1)
    }

    private fun resetLikedGenresPages(){
        likedGenresWithPageMap.replaceAll{ _, _ ->
            1
        }
        genresPreferences.savePagesForGenres(List(likedGenresWithPageMap.size) { 1 })
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
                if (result.data.isNotEmpty()) {
                    recommendedMoviesList = filterOutLikedAndSortMovies(result.data)
                    _state.update {
                        it.copy(
                            recommendedMovie = recommendedMoviesList.first(),
                            isLoading = false
                        )
                    }
                    localMovieRecommendationsRepository.addRecommendedMovies(recommendedMoviesList)
                    updateLikedMoviePage(movieId, page)
                } else {
                    // Skip entire likedMovie recommendation because it doesn't give recommendations
                    updateLikedMoviePage(movieId, 2)
                    fetchNewRecommendations()
                }

            }
        }
    }

    private fun filterOutLikedAndSortMovies(movies: List<Movie>): MutableList<Movie> {
        return movies.filterNot { it.id in likedMoviesMap.keys }.sortedBy {
            it.id
        }.toMutableList()
    }

    private suspend fun updateLikedMoviePage(movieId: Int, page: Int) {
        likedMoviesDbRepository.updatePageForLikedMovie(movieId, page + 1)
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
                recommendedMoviesList = filterOutLikedAndSortMovies(result.data)
                _state.update {
                    it.copy(
                        recommendedMovie = recommendedMoviesList.first(),
                        isLoading = false
                    )
                }
                localMovieRecommendationsRepository.addRecommendedMovies(recommendedMoviesList)
                updateGenrePage(genreId)
            }
        }
    }

    private fun updateGenrePage(genreId: String) {
        likedGenresWithPageMap[genreId] = likedGenresWithPageMap.getOrDefault(genreId, 0) + 1
        genresPreferences.savePagesForGenres(likedGenresWithPageMap.values.toList())
    }

    private fun loadLikedMovies() {
        if (likedMoviesMap.isNotEmpty()) return

        cancelLoadingJob()
        loadingLikedMoviesJob = viewModelScope.launch(ioDispatcher) {
            when (val result = likedMoviesDbRepository.getLikedMovies()) {
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

    private fun cancelLoadingJob() {
        if (loadingLikedMoviesJob.isActive) {
            loadingLikedMoviesJob.cancel()
        }
    }

    private fun loadLikedGenres(){
        likedGenresWithPageMap.putAll(genresPreferences.getGenresWithPage())
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
        _state.update {
            it.copy(isMovieScratched = true)
        }
        savedStateHandle[IS_MOVIE_SCRATCHED] = true
    }

    private fun onHeartClicked() {
        _state.update {
            it.copy(isMovieLiked = !_state.value.isMovieLiked)
        }
        viewModelScope.launch(ioDispatcher) {
            if (_state.value.isMovieLiked) {
                val result = likedMoviesDbRepository.addLikedMovie(_state.value.recommendedMovie)
                when (result) {
                    is Result.Error -> {
                        eventChannel.send(RecommendationsEvent.Error(result.error.asUiText()))
                    }

                    is Result.Success -> {
                        likedMoviesMap[_state.value.recommendedMovie.id] =
                            _state.value.recommendedMovie.toLikedMovie()

                    }
                }
            } else {
                val result = likedMoviesDbRepository.deleteLikedMovie(_state.value.recommendedMovie)
                when (result) {
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
        if (isProcessingOnReroll) return
        isProcessingOnReroll = true

        _state.update {
            it.copy(
                isMovieLiked = false,
                isMovieScratched = false
            )
        }

        savedStateHandle[IS_MOVIE_SCRATCHED] = false

        viewModelScope.launch(ioDispatcher) {
            delay(100)

            if (recommendedMoviesList.isNotEmpty()) {
                val currentMovie = recommendedMoviesList.first()

                localMovieRecommendationsRepository.deleteRecommendedMovie(currentMovie)
                recommendedMoviesList.removeAt(0)

                if (recommendedMoviesList.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            recommendedMovie = recommendedMoviesList.first(),
                            isLoading = false
                        )
                    }
                }else{
                    fetchNewRecommendations()
                }
            } else {
                fetchNewRecommendations()
            }

            isProcessingOnReroll = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        loadingLikedMoviesJob.cancel()
    }
}