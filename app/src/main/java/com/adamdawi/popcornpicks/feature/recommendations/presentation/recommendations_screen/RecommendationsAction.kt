package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

sealed interface RecommendationsAction {
    data object OnImageScratched: RecommendationsAction
    data class OnMoreInfoClicked(val movieId: String): RecommendationsAction
    data object OnHeartClicked: RecommendationsAction
    data object OnRerollClicked: RecommendationsAction
    data object OnProfileClicked: RecommendationsAction
}