package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class LikedMoviesViewModel(
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _state = MutableStateFlow(LikedMoviesState())
    val state = _state
        .onStart {

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
}