package com.adamdawi.popcornpicks.feature.recommendations.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.theme.DarkGrey
import com.adamdawi.popcornpicks.core.theme.Grey
import com.adamdawi.popcornpicks.core.theme.PopcornPicksTheme

@Composable
fun CircleIconButton(
    modifier: Modifier = Modifier,
    icon: Painter,
    onClick: () -> Unit,
    color: Color,
    enabled: Boolean,
    contentDescription: String
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .clip(CircleShape)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .background(color = if (enabled) Color.Transparent else DarkGrey)
            .border(width = 1.dp, color = if (enabled) color else Grey, shape = CircleShape),
        contentAlignment = Alignment.Center
    ){
        Icon(
            modifier = Modifier
                .fillMaxSize(0.45f),
            painter = icon,
            contentDescription = contentDescription,
            tint = if (enabled) color else Grey
        )
    }
}

@Preview
@Composable
private fun CircleIconButtonEnabledPreview() {
    PopcornPicksTheme {
        CircleIconButton(
            icon = painterResource(R.drawable.heart_ic),
            onClick = {},
            color = Color.Red,
            enabled = true,
            contentDescription = "Heart"
        )
    }
}

@Preview
@Composable
private fun CircleIconButtonDisabledPreview() {
    PopcornPicksTheme {
        CircleIconButton(
            icon = painterResource(R.drawable.heart_ic),
            onClick = {},
            color = Color.Red,
            enabled = false,
            contentDescription = "Heart"
        )
    }
}