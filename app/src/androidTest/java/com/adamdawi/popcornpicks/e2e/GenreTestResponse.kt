package com.adamdawi.popcornpicks.e2e

import com.adamdawi.popcornpicks.feature.onboarding.data.remote.response.GenreDto
import com.adamdawi.popcornpicks.feature.onboarding.data.remote.response.GenresResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun generateMockGenresResponse(): String {
    val mockResponse = GenresResponse(
        genres = listOf(
            GenreDto(
                id = 28,
                name = "Action"
            ),
            GenreDto(
                id = 12,
                name = "Adventure"
            ),
            GenreDto(
                id = 16,
                name = "Animation"
            )
        )
    )
    return Json.encodeToString(mockResponse)
}