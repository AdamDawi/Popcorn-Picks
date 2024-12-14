package com.adamdawi.popcornpicks.core.domain

interface OnBoardingManager {
    suspend fun isOnboardingCompleted(): Boolean

    suspend fun setOnboardingCompleted(completed: Boolean)
}