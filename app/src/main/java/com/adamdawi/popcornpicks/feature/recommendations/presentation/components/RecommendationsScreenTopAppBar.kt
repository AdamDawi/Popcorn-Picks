package com.adamdawi.popcornpicks.feature.recommendations.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreenTopAppBar(
    modifier: Modifier = Modifier,
    onProfileClicked: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {

        },
        actions = {
            IconButton(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape),
                onClick = onProfileClicked
            ){
                Icon(
                    modifier = Modifier.size(28.dp),
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Icon",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        )
    )
}