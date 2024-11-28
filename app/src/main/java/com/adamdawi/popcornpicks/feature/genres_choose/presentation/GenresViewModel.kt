package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GenresViewModel: ViewModel() {

    private val _state = MutableStateFlow(GenresState())
    val state = _state.asStateFlow()

    fun onAction(action: GenresAction) {
        when(action) {
            is GenresAction.SelectGenre -> TODO()
            else -> Unit
        }
    }
}