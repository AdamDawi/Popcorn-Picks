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
    private val genresPagesKey = "genre_pages_key"

    @Before
    fun setUp() {
        sharedPreferences = mockk()
        editor = mockk()
        sut = GenresPreferencesImpl(sharedPreferences)

        // Default mock behavior for editing SharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { editor.putStringSet(any(), any()) } returns editor
        every { editor.putString(any(), any()) } returns editor
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

        // Act
        sut.saveGenres(genres)

        // Assert
        verify(exactly = 1) {
            sharedPreferences.edit()
            editor.putString(genresKey, "28,12,878")
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
        val savedGenresIDs = "28,12,878"
        every { sharedPreferences.getString(genresKey, "") } returns savedGenresIDs

        // Act
        val result = sut.getGenres()

        // Assert
        assertEquals(listOf("28", "12", "878"), result)
    }

    @Test
    fun getGenres_genresIDsSavedInSharedPreferences_getStringSetInvokedOnce() {
        // Arrange
        val savedGenresIDs = "28,12,878"
        every { sharedPreferences.getString(genresKey, "") } returns savedGenresIDs

        // Act
        sut.getGenres()

        // Assert
        verify(exactly = 1) { sharedPreferences.getString(genresKey, "") }
    }

    @Test
    fun getGenres_genresIDsNotSavedInSharedPreferences_emptyListReturned() {
        // Arrange
        every { sharedPreferences.getString(genresKey, "") } returns null

        // Act
        val result = sut.getGenres()

        // Assert
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun getGenres_genresIDsNotSavedInSharedPreferences_getStringSetInvokedOnce() {
        // Arrange
        every { sharedPreferences.getString(genresKey, "") } returns null

        // Act
        sut.getGenres()

        // Assert
        verify(exactly = 1) { sharedPreferences.getString(genresKey, "") }
    }

    //PAGES for genres
    @Test
    fun savePagesForGenres_emptyListPassed_emptyListNotSaved(){
        // Arrange
        val pages = emptyList<Int>()

        // Act
        sut.savePagesForGenres(pages)

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
    fun savePagesForGenres_pagesListPassed_pagesSavedInString(){
        // Arrange
        val pages = listOf(
            1, 2, 3, 4
        )
        val pagesString = "1,2,3,4"

        // Act
        sut.savePagesForGenres(pages)

        // Assert
        verify(exactly = 1) {
            sharedPreferences.edit()
            editor.putString(genresPagesKey, pagesString)
            editor.apply()
        }
    }


    @Test
    fun getGenresWithPage_genresPagesAndGenresSavedInSharedPreferences_correctGenresPagesMapReturned() {
        // Arrange
        val savedGenresIDs = "28,12,878"
        val savedPages = "1,2,3"
        every { sharedPreferences.getString(genresKey, "") } returns savedGenresIDs
        every { sharedPreferences.getString(genresPagesKey, "") } returns savedPages

        // Act
        val result = sut.getGenresWithPage()

        // Assert
        assertEquals(
            mapOf(
                "28" to 1,
                "12" to 2,
                "878" to 3
            ),
            result
        )
    }

    @Test
    fun getGenresWithPage_genresPagesNotSavedInSharedPreferences_emptyMapReturned() {
        // Arrange
        val savedGenresIDs = "28,12,878"
        every { sharedPreferences.getString(genresKey, "") } returns savedGenresIDs
        every { sharedPreferences.getString(genresPagesKey, "") } returns null

        // Act
        val result = sut.getGenresWithPage()

        // Assert
        assertEquals(emptyMap<String, Int>(), result)
    }

    @Test
    fun getGenresWithPage_genresIdsNotSavedInSharedPreferences_emptyMapReturned() {
        // Arrange
        val savePages = ""
        every { sharedPreferences.getString(genresKey, "") } returns null
        every { sharedPreferences.getString(genresPagesKey, "") } returns savePages

        // Act
        val result = sut.getGenresWithPage()

        // Assert
        assertEquals(emptyMap<String, Int>(), result)
    }


    @Test
    fun getGenresWithPage_genresPagesSavedInSharedPreferences_getStringInvokedOnce() {
        // Arrange
        val savedPages = "1,2,3"
        val savedGenresIDs = "28,12,878"
        every { sharedPreferences.getString(genresPagesKey, "") } returns savedPages
        every { sharedPreferences.getString(genresKey, "") } returns savedGenresIDs

        // Act
        sut.getGenresWithPage()

        // Assert
        verify(exactly = 1) { sharedPreferences.getString(genresPagesKey, "") }
    }


    @Test
    fun getGenresWithPage_genresPagesSavedInSharedPreferences_getStringSetInvokedOnce() {
        // Arrange
        val savedPages = "1,2,3"
        val savedGenresIDs = "28,12,878"
        every { sharedPreferences.getString(genresPagesKey, "") } returns savedPages
        every { sharedPreferences.getString(genresKey, "") } returns savedGenresIDs

        // Act
        sut.getGenresWithPage()

        // Assert
        verify(exactly = 1) { sharedPreferences.getString(genresKey, "") }
    }
}