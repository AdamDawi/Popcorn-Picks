package com.adamdawi.popcornpicks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.adamdawi.popcornpicks.ui.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.ui.theme.fontFamily

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PopcornPicksTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) { innerPadding ->
                    Text(
                        text = "Popcorn Picks",
                        modifier = Modifier.padding(innerPadding),
                        fontFamily = fontFamily
                    )
                }
            }
        }
    }
}