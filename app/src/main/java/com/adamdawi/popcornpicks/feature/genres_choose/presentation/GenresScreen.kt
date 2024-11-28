package com.adamdawi.popcornpicks.feature.genres_choose.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.core.dummy.dummyGenres
import com.adamdawi.popcornpicks.core.dummy.selectedGenres
import com.adamdawi.popcornpicks.core.theme.DarkGrey
import com.adamdawi.popcornpicks.core.theme.LightGrey
import com.adamdawi.popcornpicks.core.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.theme.Red
import com.adamdawi.popcornpicks.core.theme.fontFamily
import com.adamdawi.popcornpicks.feature.genres_choose.domain.Genre
import kotlin.random.Random

@Composable
fun GenresScreen() {
    GenresContent(
        onAction = { action ->
            when (action) {
                is GenresAction.SelectGenre -> {
                    /*TODO*/
                }
            }
        },
        genres = dummyGenres,
        selectedGenres = selectedGenres,
        isSelectedGenresNumberValid = false
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenresContent(
    modifier: Modifier = Modifier,
    onAction: (GenresAction) -> Unit,
    genres: List<Genre>,
    selectedGenres: List<Boolean>,
    isSelectedGenresNumberValid: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Choose 2 or more of your favourite genres",
            fontFamily = fontFamily,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(64.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            for (i in genres.indices) {
                val randomGradient = remember {
                    Brush.linearGradient(
                        getHarmoniousColors()
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(
                            shape = CircleShape
                        )
                        .clickable {
                            onAction(GenresAction.SelectGenre(genres[i]))
                        }
                        .then(
                            if (selectedGenres[i]) Modifier.background(randomGradient)
                            else Modifier.background(DarkGrey)
                        )
                        .padding(8.dp)
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = genres[i].name,
                        fontFamily = fontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = if (isSelectedGenresNumberValid) "All good Press \"continue\""
            else "Please select at least 2 genres",
            fontFamily = fontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = LightGrey
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                /*TODO*/
            },
            shape = CircleShape,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Red
            )
        ) {
            Text(
                text = "Continue",
                fontFamily = fontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }


}

private fun getHarmoniousColors(): List<Color> {
    val baseHue = Random.nextFloat() * 360f
    return listOf(
        Color.hsl(baseHue, 0.8f, 0.5f), // Light color
        Color.hsl((baseHue + 30f) % 360f, 0.6f, 0.4f), // The second color with a shifted hue
        Color.hsl((baseHue + 60f) % 360f, 0.7f, 0.6f)  // Third color
    )
}

@Preview
@Composable
private fun GenresScreenPreview() {
    PopcornPicksTheme {
        GenresContent(
            genres = dummyGenres,
            selectedGenres = selectedGenres,
            isSelectedGenresNumberValid = false,
            onAction = {}
        )
    }
}