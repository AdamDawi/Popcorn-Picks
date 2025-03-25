package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.data.mapper.mapGenreIdsToGenre
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber

class ProfileViewModel(
    private val genresPreferences: GenresPreferences
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.onStart {
        getGenres()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

    fun onAction(action: ProfileAction) {
        when (action) {
            else -> {}
        }
    }

    private fun getGenres() {
        var listOfGenresIds: List<Int> = emptyList()
        //mapping to ints can throw exception
        try {
            listOfGenresIds = genresPreferences.getGenres().map {
                it.toInt()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        _state.update {
            it.copy(genres = mapGenreIdsToGenre(listOfGenresIds))
        }
    }
}