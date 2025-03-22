package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.data.dummy.genreToDrawableMap
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.SMART_FLOW_ROW
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme

// SmartFlowRow: Arranges items in rows, optimizing the use of available width.
// Items are sorted by width and assigned to rows to best fit the available space.
@Composable
fun SmartFlowRow(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier
            .testTag(SMART_FLOW_ROW)
            .fillMaxWidth(),
        content = content
    ) { measurables, constraints ->
        val itemSpacingPx = itemSpacing.roundToPx()
        val maxWidth = constraints.maxWidth

        val placeables = measurables
            .map { it.measure(Constraints(0, maxWidth)) }
            .sortedByDescending { it.width }

        val rows = mutableListOf<MutableList<Placeable>>()
        val rowWidths = mutableListOf<Int>()

        for (placeable in placeables) {
            val itemWidth = placeable.width
            var placed = false

            for (i in rows.indices) {
                if (rowWidths[i] + itemWidth + (rows[i].size * itemSpacingPx) <= maxWidth) {
                    rows[i].add(placeable)
                    rowWidths[i] += itemWidth
                    placed = true
                    break
                }
            }

            if (!placed) {
                rows.add(mutableListOf(placeable))
                rowWidths.add(itemWidth)
            }
        }

        val totalHeight = if (rows.isNotEmpty()) {
            rows.sumOf { row -> row.maxOfOrNull { it.height } ?: 0 } + (rows.size - 1) * itemSpacingPx
        } else {
            0
        }

        layout(maxWidth, totalHeight) {
            var yOffset = 0
            for (row in rows) {
                var xOffset = 0
                for (placeable in row) {
                    placeable.placeRelative(x = xOffset, y = yOffset)
                    xOffset += placeable.width + itemSpacingPx
                }
                yOffset += row.maxOf { it.height } + itemSpacingPx
            }
        }
    }
}

@Preview
@Composable
private fun SmartFlowRowPreview() {
    PopcornPicksTheme {
        SmartFlowRow {
            dummyGenresList.forEach { genre ->
                ImageLabelChip(
                    modifier = Modifier,
                    imageId = genreToDrawableMap[genre.name]
                        ?: R.drawable.horror,
                    label = genre.name
                )
            }
        }
    }
}