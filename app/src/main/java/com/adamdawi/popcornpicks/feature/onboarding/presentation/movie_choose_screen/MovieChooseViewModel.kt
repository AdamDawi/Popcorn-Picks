package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.OnBoardingManager
import com.adamdawi.popcornpicks.core.domain.repository.MoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.onboarding.domain.Movie
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.MovieChooseRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieChooseViewModel(
    private val repository: MovieChooseRepository,
    private val genresPreferences: GenresPreferences,
    private val onBoardingManager: OnBoardingManager,
    private val moviesDbRepositoryImpl: MoviesDbRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MovieChooseState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<MovieChooseEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        getMovies(getGenresIDs())
    }

    fun onAction(action: MovieChooseAction) {
        when (action) {
            is MovieChooseAction.ToggleMovieSelection -> onMovieClick(action.movie)
            is MovieChooseAction.OnFinishClick -> {
                addMoviesToDb()
            }
        }
    }

    private fun getGenresIDs(): List<String> {
        val result = genresPreferences.getGenres()
        return if (result.isEmpty()) {
            Constants.Local.DEFAULT_GENRES_IDS
        } else {
            result
        }
    }

    private fun getMovies(genresIds: List<String>) {
        _state.value = _state.value.copy(
            isLoading = true
        )
        for (i in 1..3) {
            for (genre in genresIds) {
                viewModelScope.launch {
                    val result = repository.getMovies(genre, i)

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
                                    movies = result.data + _state.value.movies,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onMovieClick(movie: Movie) {
        if (_state.value.selectedMovies.contains(movie)) {
            _state.value = _state.value.copy(
                selectedMovies = _state.value.selectedMovies - movie
            )
        } else {
            _state.value = _state.value.copy(
                selectedMovies = _state.value.selectedMovies + movie
            )
        }
        _state.value = _state.value.copy(
            finishButtonEnabled = _state.value.selectedMovies.size >= 3
        )
    }

    private fun addMoviesToDb() {
        viewModelScope.launch {
            val result = moviesDbRepositoryImpl.addMovies(_state.value.selectedMovies)
            when (result) {
                is Result.Error -> {
                    eventChannel.send(MovieChooseEvent.Error(result.error.asUiText()))
                }

                is Result.Success -> {
                    setOnboardingCompletedToTrue()
                    eventChannel.send(MovieChooseEvent.Success)
                }
            }
        }
    }

    private fun setOnboardingCompletedToTrue() {
        onBoardingManager.setOnboardingCompleted(true)
    }
}