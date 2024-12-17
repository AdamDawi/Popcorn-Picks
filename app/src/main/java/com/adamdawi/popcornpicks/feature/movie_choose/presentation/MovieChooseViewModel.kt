package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie
import com.adamdawi.popcornpicks.feature.movie_choose.domain.repository.MovieChooseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieChooseViewModel(
    private val repository: MovieChooseRepository,
    private val genresPreferences: GenresPreferences
): ViewModel() {
    private val _state = MutableStateFlow(MovieChooseState())
    val state = _state.asStateFlow()

    init {
        getMovies(getGenresIDs())
    }

    fun onAction(action: MovieChooseAction) {
        when(action) {
            is MovieChooseAction.ToggleMovieSelection -> onMovieClick(action.movie)
            else -> Unit
        }
    }

    private fun getGenresIDs(): List<String> {
        val result = genresPreferences.getGenres()
        return if(result.isEmpty()){
            Constants.Local.DEFAULT_GENRES_IDS
        }else{
            result
        }
    }

    private fun getMovies(genresIds: List<String>) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            val result = repository.getMovies(genresIds)

            when(result){
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        error = result.error.asUiText(),
                        isLoading = false
                    )
                }
                is Result.Success -> {
                    _state.value = _state.value.copy(
                        movies = result.data,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun onMovieClick(movie: Movie){
        if(_state.value.selectedMovies.contains(movie)){
            _state.value = _state.value.copy(
                selectedMovies = _state.value.selectedMovies - movie
            )
        }else{
            _state.value = _state.value.copy(
                selectedMovies = _state.value.selectedMovies + movie
            )
        }
        _state.value = _state.value.copy(
            finishButtonEnabled = _state.value.selectedMovies.size >= 3
        )
    }
}