package com.adamdawi.popcornpicks.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.IMAGE_SCRATCH

fun ComposeTestRule.scratchImage(
    stepX: Float = 50f  // Horizontal distance between successive vertical dredges
) {
    val node = onNodeWithTag(IMAGE_SCRATCH).fetchSemanticsNode()
    val bounds = node.boundsInRoot

    val startX = bounds.left
    val endX = bounds.right
    val topY = bounds.top
    val bottomY = bounds.bottom

    var currentX = startX

    while (currentX < endX) {
        onNodeWithTag(IMAGE_SCRATCH).performTouchInput {
            val start = Offset(currentX, topY)
            val end = Offset(currentX, bottomY)

            down(start)
            moveTo(end)
            up()
        }

        currentX += stepX
    }
}