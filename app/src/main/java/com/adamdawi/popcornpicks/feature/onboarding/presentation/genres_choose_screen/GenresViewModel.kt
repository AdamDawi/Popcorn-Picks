package com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.onboarding.domain.Genre
import com.adamdawi.popcornpicks.feature.onboarding.domain.repository.GenresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenresViewModel(
    private val genresRepository: GenresRepository,
    private val genresPreferences: GenresPreferences
): ViewModel() {

    private val _state = MutableStateFlow(GenresState())
    val state = _state.asStateFlow()

    init {
        getGenres()
    }

    fun onAction(action: GenresAction) {
        when(action) {
            is GenresAction.OnContinueClick -> saveGenresToPreferences()
            is GenresAction.ToggleGenreSelection -> onGenreClick(action.genre)
            else -> Unit
        }
    }

    private fun getGenres(){
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            val result = genresRepository.getGenres()
            when(result){
                is Result.Error ->{
                    _state.value = _state.value.copy(
                        error = result.error.asUiText(),
                        isLoading = false
                    )
                }
                is Result.Success ->{
                    _state.value = _state.value.copy(
                        genres = result.data,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun saveGenresToPreferences() {
        genresPreferences.saveGenres(_state.value.selectedGenres)
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