package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.POPUP_BOX

@Composable
fun PopupDialog(
    showPopup: Boolean,
    onDismiss: () -> Unit,
    content: @Composable() () -> Unit
) {
    if (showPopup) {
        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = false,
                dismissOnBackPress = true
            )
        ) {
            Box(
                modifier = Modifier
                    .testTag(POPUP_BOX)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                content()
            }
        }
    }
}