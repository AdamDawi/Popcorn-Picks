package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.TEXT_CHIP
import com.adamdawi.popcornpicks.core.presentation.theme.Grey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme

@Composable
fun TextChip(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier
            .testTag(TEXT_CHIP)
            .border(1.dp, Grey, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ){
        Text(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .padding(horizontal = 8.dp),
            text = text,
            color = Color.White.copy(alpha = .6f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun TextChipPreview() {
    PopcornPicksTheme {
        TextChip(
            modifier = Modifier,
            text = "1h 15min"
        )
    }
}