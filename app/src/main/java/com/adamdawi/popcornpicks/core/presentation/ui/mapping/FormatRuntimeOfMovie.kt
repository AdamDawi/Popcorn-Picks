package com.adamdawi.popcornpicks.core.presentation.ui.mapping

fun formatRuntime(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return "${hours}h ${remainingMinutes}m"
}