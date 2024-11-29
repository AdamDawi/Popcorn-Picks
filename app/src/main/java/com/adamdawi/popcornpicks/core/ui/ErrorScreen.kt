package com.adamdawi.popcornpicks.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.theme.fontFamily

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(140.dp),
            painter = painterResource(R.drawable.baseline_warning_24),
            contentDescription = null,
            tint = Color.White
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = if (message != null) message else "Something went wrong",
            fontFamily = fontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    PopcornPicksTheme {
        ErrorScreen()
    }
}