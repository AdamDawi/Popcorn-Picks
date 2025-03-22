package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamdawi.popcornpicks.core.data.dummy.profileImages
import com.adamdawi.popcornpicks.core.presentation.theme.ImageRed
import io.mhssn.colorpicker.ColorPicker

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProfileImageEditContent(
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    //TODO make tests for this composable
    val pickedColor = remember { mutableStateOf(ImageRed) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier,
                onClick = onSaveClick
            ) {
                Text("Save")
            }
            Button(
                modifier = Modifier,
                onClick = onCancelClick
            ) {
                Text("Cancel")
            }
        }
        ColorPicker(
            onPickedColor = { color ->
                pickedColor.value = color
            }
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(profileImages, key = { it }) { profileImage ->
                ProfileImage(
                    modifier = Modifier.size(150.dp),
                    backgroundColor = pickedColor.value,
                    onClick = {},
                    imageId = profileImage,
                    showEditIcon = false
                )
            }
        }
    }
}