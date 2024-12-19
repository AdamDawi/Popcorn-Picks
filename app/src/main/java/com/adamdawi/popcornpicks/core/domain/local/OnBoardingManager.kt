package com.adamdawi.popcornpicks.core.domain.local

interface OnBoardingManager {
    fun isOnboardingCompleted(): Boolean

    fun setOnboardingCompleted(completed: Boolean)
}