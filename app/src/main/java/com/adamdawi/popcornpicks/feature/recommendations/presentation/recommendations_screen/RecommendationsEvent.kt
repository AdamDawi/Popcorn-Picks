package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

interface RecommendationsEvent {
    data object Success: RecommendationsEvent
    data class Error(val error: String): RecommendationsEvent
}