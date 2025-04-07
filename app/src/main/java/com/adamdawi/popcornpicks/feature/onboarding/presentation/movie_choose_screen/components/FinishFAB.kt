package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.FINISH_FAB
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.FINISH_FAB_DISABLED
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.FINISH_FAB_ENABLED
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.theme.Red

@Composable
fun FinishFAB(
    modifier: Modifier = Modifier,
    isTextExpanded: Boolean,
    onFinishClick: () -> Unit,
    enabled: Boolean
) {
    ExtendedFloatingActionButton(
        expanded = isTextExpanded,
        onClick = { if(enabled) onFinishClick() },
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
            .testTag(FINISH_FAB)
            .semantics{
                contentDescription = if(enabled) FINISH_FAB_ENABLED else FINISH_FAB_DISABLED
            }
    )
}

@Preview
@Composable
private fun FinishFABExpandedPreview() {
    PopcornPicksTheme {
        FinishFAB(
            isTextExpanded = true,
            onFinishClick = {},
            enabled = true
        )
    }
}

@Preview
@Composable
private fun FinishFABNotExpandedPreview() {
    PopcornPicksTheme {
        FinishFAB(
            isTextExpanded = false,
            onFinishClick = {},
            enabled = true
        )
    }
}