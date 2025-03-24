package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.adamdawi.popcornpicks.core.presentation.theme.ImageRed
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
    val pickedColor = remember { mutableStateOf(ImageRed) }
    val selectedProfileImageId = remember { mutableIntStateOf(profileImages[0]) }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.padding(top = 16.dp))
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
            ProfileImage(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = {},
                imageId = selectedProfileImageId.intValue,
                backgroundColor = pickedColor.value,
                showEditIcon = false
            )
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
                    modifier = Modifier.size(150.dp),
                    backgroundColor = Color.White,
                    onClick = {
                        selectedProfileImageId.intValue = profileImageId
                    },
                    imageId = profileImageId,
                    showEditIcon = false,
                    isSelected = selectedProfileImageId.intValue == profileImageId
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