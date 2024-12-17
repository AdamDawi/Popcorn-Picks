package com.adamdawi.popcornpicks.core.data.local

import android.content.SharedPreferences
import com.adamdawi.popcornpicks.core.domain.OnBoardingManager

class OnBoardingManagerImpl(
    private val sharedPreferences: SharedPreferences
) : OnBoardingManager {
    override fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    override fun setOnboardingCompleted(completed: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_ONBOARDING_COMPLETED, completed)
            .apply()
    }

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
    }
}