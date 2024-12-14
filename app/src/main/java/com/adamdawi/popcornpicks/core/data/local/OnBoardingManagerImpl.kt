package com.adamdawi.popcornpicks.core.data.local

import android.content.SharedPreferences
import com.adamdawi.popcornpicks.core.domain.OnBoardingManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OnBoardingManagerImpl(
    private val sharedPreferences: SharedPreferences
): OnBoardingManager  {
    override suspend fun isOnboardingCompleted(): Boolean {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
        }
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        withContext(Dispatchers.IO){
            sharedPreferences.edit()
                .putBoolean(KEY_ONBOARDING_COMPLETED, completed)
                .commit()
        }
    }

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "key_onboarding_completed"
    }
}