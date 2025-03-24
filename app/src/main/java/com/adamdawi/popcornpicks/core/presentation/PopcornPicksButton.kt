package com.adamdawi.popcornpicks.core.presentation

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun PopcornPicksButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    text: String
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Red,
            disabledContainerColor = Red.copy(alpha = 0.3f),
            disabledContentColor = Color.White.copy(alpha = 0.3f),
            contentColor = Color.White
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
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
        PopcornPicksButton(
            onClick = {},
            enabled = true,
            text = "Continue"
        )
    }
}

@Preview
@Composable
private fun ContinueButtonDisablePreview() {
    PopcornPicksTheme {
        PopcornPicksButton(
            onClick = {},
            enabled = false,
            text = "Continue"
        )
    }
}