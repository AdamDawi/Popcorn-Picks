package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adamdawi.popcornpicks.core.data.dummy.dummyGenresList
import com.adamdawi.popcornpicks.core.data.dummy.selectedGenres
import com.adamdawi.popcornpicks.core.presentation.theme.LightGrey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.theme.fontFamily
import com.adamdawi.popcornpicks.core.presentation.ui.ErrorScreen
import com.adamdawi.popcornpicks.core.presentation.ui.LoadingScreen
import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre
import com.adamdawi.popcornpicks.feature.genres_choose.presentation.components.ContinueButton
import com.adamdawi.popcornpicks.feature.genres_choose.presentation.components.GenreChip
import org.koin.androidx.compose.koinViewModel

@Composable
fun GenresScreen(
    onContinueClick: () -> Unit,
    viewModel: GenresViewModel = koinViewModel<GenresViewModel>()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    when {
        state.value.isLoading -> LoadingScreen()
        state.value.error != null -> ErrorScreen(message = state.value.error)
        state.value.genres.isEmpty() -> ErrorScreen(message = state.value.error)
        else -> GenresContent(
            onAction = { action ->
                when (action) {
                    is GenresAction.OnContinueClick -> onContinueClick()
                    else -> viewModel.onAction(action)
                }
            },
            state = state.value
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenresContent(
    state: GenresState,
    onAction: (GenresAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        GenresTitle()

        Spacer(modifier = Modifier.height(64.dp))

        GenresFlowRow(state.genres, state.selectedGenres, onAction)

        Spacer(modifier = Modifier.height(64.dp))

        GenresValidationMessage(state.continueButtonEnabled)

        Spacer(modifier = Modifier.height(4.dp))

        ContinueButton(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(0.7f),
            enabled = state.continueButtonEnabled,
            onClick = { onAction(GenresAction.OnContinueClick) }
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun GenresTitle() {
    Text(
        text = "Choose 2 or more of your favourite genres",
        fontFamily = fontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenresFlowRow(
    genres: List<Genre>,
    selectedGenres: List<Genre>,
    onAction: (GenresAction) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        genres.forEachIndexed { index, genre ->
            GenreChip(
                genre = genre,
                isSelected = selectedGenres.contains(genre),
                onClick = { onAction(GenresAction.ToggleGenreSelection(genre)) }
            )
        }
    }
}

@Composable
private fun GenresValidationMessage(isValid: Boolean) {
    Text(
        text = if (isValid) "All good Press \"Continue\"" else "Please select at least 2 genres",
        fontFamily = fontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = LightGrey
    )
}

@Preview
@Composable
private fun GenresScreenPreview() {
    PopcornPicksTheme {
        GenresContent(
            state = GenresState(
                genres = dummyGenresList,
                selectedGenres = selectedGenres
            ),
            onAction = {}
        )
    }
}