package com.adamdawi.popcornpicks.core.data.local

import android.content.SharedPreferences
import com.adamdawi.popcornpicks.core.domain.local.GenresPreferences
import com.adamdawi.popcornpicks.core.domain.model.Genre
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class GenresPreferencesImplTest {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sut: GenresPreferences
    private val genresKey = "genres_key"

    @Before
    fun setUp() {
        sharedPreferences = mockk()
        editor = mockk()
        sut = GenresPreferencesImpl(sharedPreferences)

        // Default mock behavior for editing SharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { editor.putStringSet(any(), any()) } returns editor
        every { editor.apply() } just Runs
    }

    @Test
    fun saveGenres_genresListPassed_genresSavedInStringList() {
        // Arrange
        val genres = listOf(
            Genre(id = 28, name = "Action"),
            Genre(id = 12, name = "Adventure"),
            Genre(id = 878, name = "Science Fiction")
        )
        val genresStringList = listOf(
            "28",
            "12",
            "878"
        )

        // Act
        sut.saveGenres(genres)

        // Assert
        verify(exactly = 1) {
            sharedPreferences.edit()
            editor.putStringSet(genresKey, genresStringList.toSet())
            editor.apply()
        }
    }

    @Test
    fun saveGenres_emptyListPassed_emptyListNotSaved() {
        // Arrange
        val genres = emptyList<Genre>()

        // Act
        sut.saveGenres(genres)

        // Assert
        verify(exactly = 0) {
            sharedPreferences.edit()
        }
        verify(exactly = 0) {
            editor.putStringSet(any(), any())
        }
        verify(exactly = 0) {
            editor.apply()
        }
    }

    @Test
    fun getGenres_genresIDsSavedInSharedPreferences_correctGenresIDsListReturned() {
        // Arrange
        val savedGenresIDs = setOf("28", "12", "878")
        every { sharedPreferences.getStringSet(genresKey, emptySet()) } returns savedGenresIDs

        // Act
        val result = sut.getGenres()

        // Assert
        assertEquals(listOf("28", "12", "878"), result)
    }

    @Test
    fun getGenres_genresIDsSavedInSharedPreferences_getStringSetInvokedOnce() {
        // Arrange
        val savedGenresIDs = setOf("28", "12", "878")
        every { sharedPreferences.getStringSet(genresKey, emptySet()) } returns savedGenresIDs

        // Act
        sut.getGenres()

        // Assert
        verify(exactly = 1) { sharedPreferences.getStringSet(genresKey, emptySet()) }
    }

    @Test
    fun getGenres_genresIDsNotSavedInSharedPreferences_emptyListReturned() {
        // Arrange
        every { sharedPreferences.getStringSet(genresKey, emptySet()) } returns null

        // Act
        val result = sut.getGenres()

        // Assert
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun getGenres_genresIDsNotSavedInSharedPreferences_getStringSetInvokedOnce() {
        // Arrange
        every { sharedPreferences.getStringSet(genresKey, emptySet()) } returns null

        // Act
        sut.getGenres()

        // Assert
        verify(exactly = 1) { sharedPreferences.getStringSet(genresKey, emptySet()) }
    }
}