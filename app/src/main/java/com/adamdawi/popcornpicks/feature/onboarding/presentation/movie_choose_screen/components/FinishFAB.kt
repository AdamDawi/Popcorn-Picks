package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.presentation.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishFAB(
    modifier: Modifier = Modifier,
    showText: Boolean,
    onFinishClick: () -> Unit,
    enabled: Boolean
) {
    ExtendedFloatingActionButton(
        expanded = showText,
        onClick = { onFinishClick() },
        containerColor = if(enabled) Red else Red.copy(alpha = 0.3f),
        text = {
            Text(
                text = "Finish",
                color = if(enabled) Color.White else Color.White.copy(alpha = 0.3f),
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
                tint = if(enabled) Color.White else Color.White.copy(alpha = 0.3f)
            )
        },
        modifier = modifier
            .background(color = Color.Black, shape = FloatingActionButtonDefaults.extendedFabShape)
    )
}