package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.data.dummy.profileImages
import com.adamdawi.popcornpicks.core.presentation.PopcornPicksButton
import com.adamdawi.popcornpicks.core.presentation.theme.Blue
import com.adamdawi.popcornpicks.core.presentation.theme.Grey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import io.mhssn.colorpicker.ColorPicker
import io.mhssn.colorpicker.ColorPickerType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileImageEditContent(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    //TODO make tests for this composable
    val pickedColor = remember { mutableStateOf(Color.White) }
    val selectedProfileImageId = remember { mutableIntStateOf(profileImages[0]) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PopcornPicksButton(
                modifier = Modifier,
                onClick = onSaveClick,
                text = "Save"
            )
            PopcornPicksButton(
                modifier = Modifier,
                onClick = onCancelClick,
                text = "Cancel"
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Customize your profile image",
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ColorPicker(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                type = ColorPickerType.Circle(showAlphaBar = false, showBrightnessBar = false),
                onPickedColor = { color ->
                    pickedColor.value = color
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            AnimatedContent(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                targetState = selectedProfileImageId.intValue,
                transitionSpec = {
                    val enterAnimation = fadeIn(animationSpec = tween(300, delayMillis = 100)) +
                            scaleIn(initialScale = 0.8f, animationSpec = tween(300, delayMillis = 100))

                    val exitAnimation = fadeOut(animationSpec = tween(200, delayMillis = 50)) +
                            scaleOut(targetScale = 0.8f, animationSpec = tween(200, delayMillis = 50))

                    enterAnimation.togetherWith(exitAnimation)
                }
            ) { targetProfileImageId ->
                ProfileImage(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f),
                    onClick = {},
                    imageId = targetProfileImageId,
                    backgroundColor = pickedColor.value,
                    showEditIcon = false,
                    clickable = false
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Pick your profile image",
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(profileImages, key = { it }) { profileImageId ->
                ProfileImage(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(2.dp, color = if(profileImageId == selectedProfileImageId.intValue) Blue.copy(alpha = .5f) else Grey.copy(alpha = .5f), shape = RoundedCornerShape(2.dp)),
                    backgroundColor = if(profileImageId == selectedProfileImageId.intValue) Blue.copy(alpha = 0.4f) else Grey.copy(alpha = .4f),
                    onClick = {
                        selectedProfileImageId.intValue = profileImageId
                    },
                    imageId = profileImageId,
                    showEditIcon = false
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileImageEditContentPreview() {
    PopcornPicksTheme {
        ProfileImageEditContent(
            onSaveClick = {},
            onCancelClick = {}
        )
    }
}