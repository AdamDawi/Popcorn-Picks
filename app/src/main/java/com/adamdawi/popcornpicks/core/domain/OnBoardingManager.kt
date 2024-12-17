package com.adamdawi.popcornpicks.core.domain

interface OnBoardingManager {
    fun isOnboardingCompleted(): Boolean

    fun setOnboardingCompleted(completed: Boolean)
}