package com.adamdawi.popcornpicks.feature.movie_details.presentation.movie_details_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.util.Constants.NavArguments.MOVIE_ID
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.movie_details.data.remote.MovieDetailsRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val movieDetailsRepositoryImpl: MovieDetailsRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
    private val _state = MutableStateFlow(MovieDetailsState())
    val state = _state.onStart {
        getMovieDetails(getMovieId())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MovieDetailsState()
    )

    private fun getMovieId(): String?{
        return savedStateHandle[MOVIE_ID]
    }

    private fun getMovieDetails(movieId: String?){
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        if(movieId != null){
            viewModelScope.launch(ioDispatcher){
                when(val result = movieDetailsRepositoryImpl.getDetailedMovie(movieId)){
                    is Result.Error ->{
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
                                movie = result.data,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }else{
            _state.update {
                it.copy(
                    error = "Something went wrong while loading this movie. Please try again."
                )
            }
        }
    }
}