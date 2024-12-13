package com.adamdawi.popcornpicks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.navigation.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PopcornPicksTheme {
                Navigation()
            }
        }
    }
}