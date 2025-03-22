@file:OptIn(ExperimentalMaterial3Api::class)

package com.adamdawi.popcornpicks.core.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.TOP_APP_BAR_ACTIONS
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.TOP_APP_BAR_NAVIGATION_ICON

@Composable
fun PopcornPicksTopAppBar(
    modifier: Modifier = Modifier,
    actions: (@Composable () -> Unit)? = null,
    navigationIcon: (@Composable () -> Unit)? = null,
    titleText: String? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            titleText?.let {
                Text(
                    text = titleText,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp
                )
            }
        },
        navigationIcon = {
            navigationIcon?.let {
                Box(modifier = Modifier.testTag(TOP_APP_BAR_NAVIGATION_ICON)) {
                    it()
                }
            }
        },
        actions = {
            actions?.let {
                Box(modifier = Modifier.testTag(TOP_APP_BAR_ACTIONS)) {
                    it()
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black,
            scrolledContainerColor = Color.Black
        ),
        scrollBehavior = scrollBehavior
    )
}