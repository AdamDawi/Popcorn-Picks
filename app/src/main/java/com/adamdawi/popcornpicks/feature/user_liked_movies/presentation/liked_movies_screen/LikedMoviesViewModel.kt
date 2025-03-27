package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LikedMoviesViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val likedMoviesDbRepository: LikedMoviesDbRepository
): ViewModel() {
    private val _state = MutableStateFlow(LikedMoviesState())
    val state = _state
        .onStart {
            getLikedMovies()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _state.value
        )

    fun onAction(action: LikedMoviesAction){
        when(action){
            else ->{}
        }
    }
    private fun getLikedMovies(){
        _state.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch(ioDispatcher){
            val result = likedMoviesDbRepository.getLikedMovies()
            when(result){
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.asUiText()
                        )
                    }
                }
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            movies = result.data
                        )
                    }
                }
            }
        }
    }
}