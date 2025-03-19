@file:OptIn(ExperimentalLayoutApi::class)

package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.presentation.theme.DividerGrey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.ui.PopcornPicksTopAppBar
import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components.IconLabelChip
import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components.ProfileImage
import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components.SmartFlowRow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel<ProfileViewModel>()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    ProfileScreenContent(
        state = state.value,
        onAction = { action ->
            when (action) {
                ProfileAction.OnBackClicked -> {
                    onNavigateBack()
                }
                else -> viewModel.onAction(action = action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    Scaffold(
        topBar = {
            PopcornPicksTopAppBar(
                titleText = "Profile",
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable {
                                onAction(ProfileAction.OnBackClicked)
                            },
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Arrow back",
                        tint = Color.White
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 32.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            ProfileImage(
                modifier = Modifier.size(150.dp),
                onClick = {

                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "YOU",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(40.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = DividerGrey.copy(alpha = .6f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                modifier = Modifier.padding(14.dp),
                text = "Genres",
                fontSize = 24.sp,
                maxLines = 1,
                color = Color.White
            )
            SmartFlowRow(itemSpacing = 8.dp) {
                IconLabelChip(
                    modifier = Modifier,
                    icon = R.drawable.heart_solid_ic,
                    label = "Action"
                )

                IconLabelChip(
                    modifier = Modifier,
                    icon = R.drawable.heart_solid_ic,
                    label = "Comedy"
                )

                IconLabelChip(
                    modifier = Modifier,
                    icon = R.drawable.heart_solid_ic,
                    label = "Documentary"
                )
            }
        }
    }
}







@Preview
@Composable
private fun ProfileScreenPreview() {
    PopcornPicksTheme {
        ProfileScreenContent(
            state = ProfileState(
                genres = dummyGenresList
            ),
            onAction = {}
        )
    }
}