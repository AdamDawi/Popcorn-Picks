package com.adamdawi.popcornpicks.feature.user_liked_movies.presentation.liked_movies_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.data.dummy.dummyLikedMovie
import com.adamdawi.popcornpicks.core.domain.model.LikedMovie
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.theme.Yellow
import com.adamdawi.popcornpicks.core.presentation.ui.PosterImage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LikedMovieItem(
    modifier: Modifier = Modifier,
    movie: LikedMovie
) {
    //TODO make tests for this composable
    Row(
        modifier = modifier
            .height(180.dp)
    ) {
        PosterImage(
            modifier = Modifier
                .height(180.dp)
                .width(120.dp),
            posterUrl = movie.poster.toString()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Column {
                Text(
                    text = movie.title + " (${movie.releaseDate.take(4)})",
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    maxLines = 4,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TextChip(
                        text = movie.releaseDate.take(4)
                    )
                    movie.genres.forEach { genre ->
                        TextChip(
                            text = genre.name
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .height(16.dp)
                    .align(Alignment.BottomStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = movie.voteAverage.toString(),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    modifier = Modifier
                        .size(16.dp)
                        .aspectRatio(1f),
                    painter = painterResource(R.drawable.star_solid),
                    tint = Yellow,
                    contentDescription = "Star"
                )
            }
        }
    }
}

@Preview
@Composable
private fun LikedMovieItemPreview() {
    PopcornPicksTheme {
        LikedMovieItem(
            movie = dummyLikedMovie
        )
    }
}