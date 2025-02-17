package com.adamdawi.popcornpicks.feature.recommendations.presentation.recommendations_screen

sealed interface RecommendationsAction {
    object OnImageScratched: RecommendationsAction
    object OnMoreInfoClicked: RecommendationsAction
    object OnHeartClicked: RecommendationsAction
    object OnRerollClicked: RecommendationsAction
    object OnProfileClicked: RecommendationsAction
}