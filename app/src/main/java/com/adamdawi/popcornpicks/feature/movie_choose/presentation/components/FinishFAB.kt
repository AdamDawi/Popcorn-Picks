package com.adamdawi.popcornpicks.feature.movie_choose.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishFAB(
    modifier: Modifier = Modifier,
    showText: Boolean,
    onFinishClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        expanded = showText,
        onClick = { onFinishClick() },
        containerColor = Red,
        text = {
            Text(
                text = "Finish",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 8.dp),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        },
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = "Finish",
                tint = Color.White
            )
        },
        modifier = modifier
    )
}