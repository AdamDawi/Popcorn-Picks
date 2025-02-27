package com.adamdawi.popcornpicks.feature.onboarding.presentation.genres_choose_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.GENRE_CHIP
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.GENRE_NOT_SELECTED
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.GENRE_SELECTED
import com.adamdawi.popcornpicks.core.presentation.theme.DarkGrey
import com.adamdawi.popcornpicks.core.presentation.theme.fontFamily
import com.adamdawi.popcornpicks.core.domain.model.Genre
import kotlin.random.Random

@Composable
fun GenreChip(
    genre: Genre,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val randomGradient = remember {
        Brush.linearGradient(getHarmoniousColors())
    }
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier
                    .background(randomGradient)
                    .testTag(GENRE_CHIP)
                    .semantics {
                        contentDescription = GENRE_SELECTED
                    }
                else {
                    Modifier
                        .background(DarkGrey)
                        .testTag(GENRE_CHIP)
                        .semantics {
                            contentDescription = GENRE_NOT_SELECTED
                        }
                }

            )
            .padding(8.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre.name,
            fontFamily = fontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

private fun getHarmoniousColors(): List<Color> {
    val baseHue = Random.nextFloat() * 360f
    return listOf(
        Color.hsl(baseHue, 0.8f, 0.5f),
        Color.hsl((baseHue + 30f) % 360f, 0.6f, 0.4f),
        Color.hsl((baseHue + 60f) % 360f, 0.7f, 0.6f)
    )
}