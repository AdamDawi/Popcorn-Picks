package com.adamdawi.popcornpicks.feature.movie_details.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.presentation.theme.Blue
import com.adamdawi.popcornpicks.core.presentation.theme.LightGrey

@Composable
fun TextValueRow(
    modifier: Modifier = Modifier,
    text: String,
    value: String,
    fontSize: TextUnit = 14.sp
) {
    Row(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            color = LightGrey,
            fontSize = fontSize
        )
        Text(
            modifier = Modifier.weight(1f),
            text = value,
            color = Blue,
            fontSize = fontSize
        )
    }
}