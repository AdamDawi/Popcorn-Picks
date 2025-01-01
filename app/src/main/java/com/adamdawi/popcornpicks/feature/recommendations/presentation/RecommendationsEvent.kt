package com.adamdawi.popcornpicks.feature.recommendations.presentation

interface RecommendationsEvent {
    data object Success: RecommendationsEvent
    data class Error(val error: String): RecommendationsEvent
}