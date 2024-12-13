package com.adamdawi.popcornpicks.feature.genres_choose.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.theme.Red
import com.adamdawi.popcornpicks.core.presentation.theme.fontFamily

@Composable
fun ContinueButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Red,
            disabledContainerColor = Red.copy(alpha = 0.3f),
            disabledContentColor = Color.White.copy(alpha = 0.3f),
            contentColor = Color.White
        ),
        enabled = enabled
    ) {
        Text(
            text = "Continue",
            fontFamily = fontFamily,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview
@Composable
private fun ContinueButtonEnablePreview() {
    PopcornPicksTheme {
        ContinueButton(
            onClick = {},
            enabled = true
        )
    }
}

@Preview
@Composable
private fun ContinueButtonDisablePreview() {
    PopcornPicksTheme {
        ContinueButton(
            onClick = {},
            enabled = false
        )
    }
}