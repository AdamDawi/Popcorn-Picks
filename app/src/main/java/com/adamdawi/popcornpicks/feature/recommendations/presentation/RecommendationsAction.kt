package com.adamdawi.popcornpicks.feature.recommendations.presentation

sealed interface RecommendationsAction {
    object OnImageScratched: RecommendationsAction
    object OnMoreInfoClicked: RecommendationsAction
    object OnHeartClicked: RecommendationsAction
    object OnRetryClicked: RecommendationsAction
    object OnProfileClicked: RecommendationsAction
}