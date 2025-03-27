@file:OptIn(ExperimentalLayoutApi::class)

package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.data.dummy.genreToDrawableMap
import com.adamdawi.popcornpicks.core.data.dummy.profileImages
import com.adamdawi.popcornpicks.core.domain.model.Genre
import com.adamdawi.popcornpicks.core.presentation.theme.DividerGrey
import com.adamdawi.popcornpicks.core.presentation.theme.ImageRed
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.ui.LoadingScreen
import com.adamdawi.popcornpicks.core.presentation.ui.PopcornPicksTopAppBar
import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components.ImageLabelChip
import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components.PopupDialog
import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components.ProfileImage
import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components.ProfileImageEditContent
import com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components.SmartFlowRow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel<ProfileViewModel>()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    when {
        state.value.isLoading -> LoadingScreen()
        else -> {
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
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreenContent(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val showPopup = remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 32.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                PopupDialog(
                    showPopup = showPopup.value,
                    onDismiss = { showPopup.value = false }
                ) {
                    ProfileImageEditContent(
                        modifier = Modifier.fillMaxSize(),
                        onSaveClick = { pickedColor, pickedImageId ->
                            onAction(ProfileAction.OnSaveProfileImageStyle(pickedImageId, pickedColor.toArgb()))
                            showPopup.value = false
                        },
                        onCancelClick = {
                            showPopup.value = false
                        },
                        state.profileImageStyle?.imageId ?: profileImages[0]
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                ProfileImage(
                    modifier = Modifier.size(150.dp),
                    backgroundColor = Color(state.profileImageStyle?.backgroundColor ?: ImageRed.toArgb()),
                    onClick = {
                        showPopup.value = true
                    },
                    imageId = state.profileImageStyle?.imageId ?: profileImages[0]
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
                GenresSection(genres = state.genres)
                Spacer(modifier = Modifier.height(40.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 2.dp,
                    color = DividerGrey.copy(alpha = .6f)
                )
                MoviesSection(
                    likedMoviesCount = state.likedMoviesCount
                )
            }
        }
    }
}


@Composable
private fun GenresSection(
    genres: List<Genre>
) {
    Text(
        modifier = Modifier.padding(14.dp),
        text = "Genres",
        fontSize = 24.sp,
        maxLines = 1,
        color = Color.White
    )
    Spacer(modifier = Modifier.height(8.dp))
    if (genres.isNotEmpty()) {
        SmartFlowRow(itemSpacing = 8.dp) {
            genres.forEach { genre ->
                key(genre.name) {
                    ImageLabelChip(
                        modifier = Modifier,
                        imageId = genreToDrawableMap[genre.name] ?: R.drawable.not_found,
                        label = genre.name
                    )
                }
            }
        }
    } else {
        Text(
            "No genres available",
            color = Color.White.copy(alpha = .6f),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun MoviesSection(
    likedMoviesCount: Int?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable {

            }
            .padding(horizontal = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(14.dp)
                .align(Alignment.Center),
            text = "Movies",
            fontSize = 24.sp,
            maxLines = 1,
            color = Color.White
        )
        Icon(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterEnd),
            tint = Color.White,
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = "Go to movies"
        )
    }
    if(likedMoviesCount == null){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "No movies information available",
                color = Color.White.copy(alpha = .6f),
                fontSize = 14.sp
            )
        }
    }else{
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ImageLabelChip(
                imageId = R.drawable.heart,
                contentDescription = "Liked movies",
                label = likedMoviesCount.toString()
            )
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    PopcornPicksTheme {
        ProfileScreenContent(
            state = ProfileState(
                genres = dummyGenresList.take(5),
                likedMoviesCount = 282
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun ProfileScreenNoGenresAndNoCountPreview() {
    PopcornPicksTheme {
        ProfileScreenContent(
            state = ProfileState(
                genres = emptyList(),
                likedMoviesCount = null
            ),
            onAction = {}
        )
    }
}