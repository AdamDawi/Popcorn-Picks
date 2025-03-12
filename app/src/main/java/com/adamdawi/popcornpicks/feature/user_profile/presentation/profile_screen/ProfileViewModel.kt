package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel: ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.onStart {

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

    fun onAction(action: ProfileAction){
        when(action){
            else -> {}
        }
    }
}