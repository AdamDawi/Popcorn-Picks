package com.adamdawi.popcornpicks.feature.onboarding.data.repository

import com.adamdawi.popcornpicks.core.data.remote.GenresRepositoryImpl
import com.adamdawi.popcornpicks.core.domain.local.GenresRepository
import com.adamdawi.popcornpicks.utils.ReplaceMainDispatcherRule
import io.ktor.client.HttpClient
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

class GenresRepositoryImplTest {

    private lateinit var sut: GenresRepository
    private lateinit var httpClient: HttpClient

    @OptIn(ExperimentalCoroutinesApi::class)
    @get: Rule
    val replaceMainDispatcherRule = ReplaceMainDispatcherRule()

    @Before
    fun setUp(){
        httpClient = mockk<HttpClient>()
        sut = GenresRepositoryImpl(httpClient)
    }
    //TODO make testing for http client

//    @Test
//    fun getGenres_success_genresResponseMapedToGenres() = runTest {
//        // Arrange
//        val mockGenresResponse = GenresResponse(
//            genres = listOf(GenreDto(id = 1, name = "Action"))
//        )
//        coEvery { httpClient.get<GenresResponse>(any(), any()) } returns
//                Result<GenresResponse, DataError.Network>.Success(mockGenresResponse)
//
//        // Act
//        val result = sut.getGenres()
//
//        // Assert
//        assertThat(result, `is`(instanceOf(Result.Success::class)))
//        val successResult = result as Result.Success
//        assertThat(successResult.data.size, `is`(1))
//        assertThat(successResult.data[0].id, `is`(1))
//        assertThat(successResult.data[0].name, `is`("Action"))
//    }

//    @Test
//    fun getGenres_error_handlesNetworkError() = runTest {
//        // Arrange
//        coEvery { httpClient.get<GenresResponse>(any(), any()) } throws Exception("Network error")
//
//        val sut = GenresRepositoryImpl(httpClient)
//
//        // Act
//        val result = sut.getGenres()
//
//        // Assert
//        assertThat(result, `is`(instanceOf(Result.Error::class.java)))
//        val errorResult = result as Result.Error
//        assertThat(errorResult.error.message, `is`("Network error"))
//    }
}