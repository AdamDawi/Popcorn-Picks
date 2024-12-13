package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import androidx.lifecycle.ViewModel
import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre
import com.adamdawi.popcornpicks.feature.genres_choose.domain.repository.GenresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GenresViewModel(
    private val genresRepository: GenresRepository
): ViewModel() {

    private val _state = MutableStateFlow(GenresState())
    val state = _state.asStateFlow()

    init {
        _state.value = _state.value.copy(
            genres = getGenres()
        )
    }

    fun onAction(action: GenresAction) {
        when(action) {
            is GenresAction.ToggleGenreSelection -> onGenreClick(action.genre)
            else -> Unit
        }
    }

    private fun getGenres(): List<Genre>{
        return genresRepository.getGenres()
    }

    private fun onGenreClick(genre: Genre){
        if(_state.value.selectedGenres.contains(genre)){
            _state.value = _state.value.copy(
                selectedGenres = _state.value.selectedGenres - genre
            )
        }else{
            _state.value = _state.value.copy(
                selectedGenres = _state.value.selectedGenres + genre
            )
        }
        _state.value = _state.value.copy(
            continueButtonEnabled = _state.value.selectedGenres.size >= 2
        )
    }
}