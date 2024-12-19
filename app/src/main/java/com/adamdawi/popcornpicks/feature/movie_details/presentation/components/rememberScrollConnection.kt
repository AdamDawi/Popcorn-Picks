package com.adamdawi.popcornpicks.feature.movie_details.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

@Composable
fun rememberScrollConnection(
    maxOffset: Float,
    currentOffsetY: MutableState<Float>
): NestedScrollConnection {
    return remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val scrollSpeedFactor = 0.1f
                val delta = available.y * scrollSpeedFactor
                val newOffsetY = (currentOffsetY.value + delta).coerceIn(-maxOffset, 0f)
                val consumed = newOffsetY - currentOffsetY.value
                currentOffsetY.value = newOffsetY
                return Offset(0f, consumed)
            }
        }
    }
}
