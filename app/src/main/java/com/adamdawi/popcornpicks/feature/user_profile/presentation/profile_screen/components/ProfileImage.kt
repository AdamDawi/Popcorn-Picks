package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.EDIT_ICON
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.PROFILE_IMAGE
import com.adamdawi.popcornpicks.core.presentation.theme.BorderGrey
import com.adamdawi.popcornpicks.core.presentation.theme.Green
import com.adamdawi.popcornpicks.core.presentation.theme.ImageRed
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color,
    @DrawableRes imageId: Int = R.drawable.happy_guard,
    showEditIcon: Boolean = true,
    isSelected: Boolean = false,
    clickable: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(backgroundColor)
            .clickable(enabled = clickable) {
                onClick()
            }
            .padding(12.dp)
            .testTag(PROFILE_IMAGE)

    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(imageId),
            contentDescription = "profile image",
            contentScale = ContentScale.FillBounds
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopEnd),
            visible = isSelected,
            enter = fadeIn(animationSpec = tween(durationMillis = 250)) + slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(durationMillis = 250)),
            exit = fadeOut(animationSpec = tween(durationMillis = 150)) + slideOutVertically(targetOffsetY = { -40 }, animationSpec = tween(durationMillis = 150))
        ) {
            Icon(
                modifier = Modifier.align(Alignment.TopEnd),
                imageVector = Icons.Default.Done,
                tint = Green,
                contentDescription = "Selected"
            )
        }
        if(showEditIcon){
            Box(
                modifier = Modifier
                    .testTag(EDIT_ICON)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = BorderGrey, shape = CircleShape)
                    .align(Alignment.BottomStart)
                    .background(Color.Black)
                    .padding(5.dp)
            ){
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(R.drawable.pen_ic),
                    tint = Color.White,
                    contentDescription = "edit image"
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProfileImagePreview() {
    PopcornPicksTheme {
        ProfileImage(
            modifier = Modifier.size(150.dp),
            backgroundColor = ImageRed,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun ProfileImageSelectedPreview() {
    PopcornPicksTheme {
        ProfileImage(
            modifier = Modifier.size(150.dp),
            backgroundColor = ImageRed,
            onClick = {},
            isSelected = true
        )
    }
}