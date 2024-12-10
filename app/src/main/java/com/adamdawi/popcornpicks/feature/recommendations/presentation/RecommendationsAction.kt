package com.adamdawi.popcornpicks.feature.recommendations.presentation

sealed interface RecommendationsAction {
    object OnImageScratched: RecommendationsAction
}