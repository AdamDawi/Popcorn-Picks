package com.adamdawi.popcornpicks.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.LOADING_SCREEN
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .testTag(LOADING_SCREEN)
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    PopcornPicksTheme {
        LoadingScreen()
    }
}