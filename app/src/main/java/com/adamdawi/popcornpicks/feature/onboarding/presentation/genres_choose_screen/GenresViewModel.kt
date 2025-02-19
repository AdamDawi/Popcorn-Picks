package com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.feature.onboarding.domain.remote.GenresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GenresViewModel(
    private val genresRepository: GenresRepository,
    private val genresPreferences: GenresPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(GenresState())
    val state = _state.onStart {
        getGenres()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        _state.value
    )

    fun onAction(action: GenresAction) {
        when (action) {
            is GenresAction.OnContinueClick -> saveGenresToPreferences()
            is GenresAction.ToggleGenreSelection -> onGenreClick(action.genre)
        }
    }

    private fun getGenres() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            val result = genresRepository.getGenres()
            when (result) {
                is Result.Error -> {
                    _state.value = _state.value.copy(
                        error = result.error.asUiText(),
                        isLoading = false
                    )
                }

                is Result.Success -> {
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
        genresPreferences.savePagesForGenres(List(_state.value.selectedGenres.size) { 1 }) // Save default page value
    }

    private fun onGenreClick(genre: Genre) {
        if (_state.value.selectedGenres.contains(genre)) {
            _state.value = _state.value.copy(
                selectedGenres = _state.value.selectedGenres - genre,
                continueButtonEnabled = _state.value.selectedGenres.size-1 >= 2
            )
        } else {
            _state.value = _state.value.copy(
                selectedGenres = _state.value.selectedGenres + genre,
                continueButtonEnabled = _state.value.selectedGenres.size+1 >= 2
            )
        }
    }
}