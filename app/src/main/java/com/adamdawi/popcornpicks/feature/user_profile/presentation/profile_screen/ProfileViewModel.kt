package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamdawi.popcornpicks.core.data.mapper.mapGenreIdsToGenre
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.local.LikedMoviesDbRepository
import com.adamdawi.popcornpicks.core.domain.util.Result
import com.adamdawi.popcornpicks.core.presentation.ui.mapping.asUiText
import com.adamdawi.popcornpicks.feature.user_profile.domain.ProfileImageStyle
import com.adamdawi.popcornpicks.feature.user_profile.domain.local.UserProfilePreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(
    private val genresPreferences: GenresPreferences,
    private val userProfilePreferences: UserProfilePreferences,
    private val likedMoviesDbRepository: LikedMoviesDbRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.onStart {
        getProfileImageStyle()
        getLikedMoviesCount()
        getGenres()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnSaveProfileImageStyle -> {
                saveProfileImageStyle(action.profileImageId, action.profileImageBg)
            }
            else -> {}
        }
    }

    private fun getGenres() {
        viewModelScope.launch(ioDispatcher) {
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

    private fun getLikedMoviesCount(){
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch(ioDispatcher){
            val result = likedMoviesDbRepository.getLikedMoviesCount()
            when(result){
                is Result.Error -> {
                    Timber.e(result.error.asUiText())
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
                is Result.Success -> {
                    _state.update {
                        it.copy(likedMoviesCount = result.data, isLoading = false)
                    }
                }
            }
        }
    }

    private fun getProfileImageStyle(){
        viewModelScope.launch(ioDispatcher) {
            val newProfileImageStyle = userProfilePreferences.getProfileImageStyle()
            _state.update {
                it.copy(profileImageStyle = newProfileImageStyle)
            }
        }
    }

    private fun saveProfileImageStyle(profileImageId: Int, profileImageBg: Int){
        viewModelScope.launch(ioDispatcher) {
            _state.update {
                it.copy(profileImageStyle = ProfileImageStyle(profileImageId, profileImageBg))
            }
            userProfilePreferences.saveProfileImageStyle(
                profileImageStyle = ProfileImageStyle(
                    profileImageId,
                    profileImageBg
                )
            )
        }
    }
}