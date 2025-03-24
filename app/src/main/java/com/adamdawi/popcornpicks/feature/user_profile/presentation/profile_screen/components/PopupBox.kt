package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.POPUP_BOX

@Composable
fun PopupBox(showPopup: Boolean, content: @Composable() () -> Unit) {
    if (showPopup) {
        Box(
            modifier = Modifier
                .testTag(POPUP_BOX)
                .fillMaxSize()
                .zIndex(10F),
            contentAlignment = Alignment.Center
        ) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(
                    excludeFromSystemGesture = true,
                )
            ) {
                content()
            }
        }
    }
}