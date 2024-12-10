package com.adamdawi.popcornpicks.feature.movie_choose.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieChooseViewModel: ViewModel() {
    private val _state = MutableStateFlow(MovieChooseState())
    val state = _state.asStateFlow()

    fun onAction(action: MovieChooseAction) {
        when(action) {
            is MovieChooseAction.SelectMovie -> TODO()
            else -> Unit
        }
    }


}