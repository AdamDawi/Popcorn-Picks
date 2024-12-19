package com.adamdawi.popcornpicks.core.data.local

import android.content.SharedPreferences
import com.adamdawi.popcornpicks.core.domain.local.OnBoardingManager
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class OnBoardingManagerImplTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sut: OnBoardingManager
    private val onboardingKey = "key_onboarding_completed"

    @Before
    fun setUp() {
        sharedPreferences = mockk()
        editor = mockk()
        sut = OnBoardingManagerImpl(sharedPreferences)

        // Default mock behavior for editing SharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.apply() } just Runs
    }

    @Test
    fun setOnboardingCompleted_trueBooleanPassed_trueBooleanSaved() {
        // Arrange
        val completed = true

        // Act
        sut.setOnboardingCompleted(completed)

        // Assert
        verify(exactly = 1) {
            sharedPreferences.edit()
            editor.putBoolean(onboardingKey, completed)
            editor.apply()
        }
    }

    @Test
    fun setOnboardingCompleted_falseBooleanPassed_falseBooleanSaved() {
        // Arrange
        val completed = false

        // Act
        sut.setOnboardingCompleted(completed)

        // Assert
        verify(exactly = 1) {
            sharedPreferences.edit()
            editor.putBoolean(onboardingKey, completed)
            editor.apply()
        }
    }

    @Test
    fun isOnboardingCompleted_booleanSavedInSharedPreferences_correctBooleanReturned() {
        // Arrange
        val expectedValue = true
        every { sharedPreferences.getBoolean(onboardingKey, false) } returns expectedValue

        // Act
        val result = sut.isOnboardingCompleted()

        // Assert
        assertEquals(expectedValue, result)
    }

    @Test
    fun isOnboardingCompleted_booleanSavedInSharedPreferences_getBooleanInvokedOnce() {
        // Arrange
        val expectedValue = true
        every { sharedPreferences.getBoolean(onboardingKey, false) } returns expectedValue

        // Act
        sut.isOnboardingCompleted()

        // Assert
        verify(exactly = 1) { sharedPreferences.getBoolean(onboardingKey, false) }
    }

    @Test
    fun isOnboardingCompleted_booleanNotSavedInSharedPreferences_defaultValueReturned() {
        // Arrange
        every { sharedPreferences.getBoolean(onboardingKey, false) } returns false

        // Act
        val result = sut.isOnboardingCompleted()

        // Assert
        assertEquals(false, result)
    }

    @Test
    fun isOnboardingCompleted_booleanNotSavedInSharedPreferences_getBooleanInvokedOnce() {
        // Arrange
        every { sharedPreferences.getBoolean(onboardingKey, false) } returns false

        // Act
        sut.isOnboardingCompleted()

        // Assert
        verify(exactly = 1) { sharedPreferences.getBoolean(onboardingKey, false) }
    }
}
