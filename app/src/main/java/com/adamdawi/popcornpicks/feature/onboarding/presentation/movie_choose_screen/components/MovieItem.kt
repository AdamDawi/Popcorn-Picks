package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.data.dummy.dummyMovie
import com.adamdawi.popcornpicks.core.domain.util.Constants.Network.BASE_IMAGE_URL
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.IMAGE_WITH_ANIMATED_BORDER
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_ITEM
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.REGULAR_IMAGE
import com.adamdawi.popcornpicks.core.presentation.theme.Blue
import com.adamdawi.popcornpicks.core.presentation.theme.DarkGrey
import com.adamdawi.popcornpicks.core.presentation.theme.LightGrey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme
import com.adamdawi.popcornpicks.core.presentation.theme.fontFamily
import com.adamdawi.popcornpicks.core.presentation.ui.shimmerBrush
import com.adamdawi.popcornpicks.core.domain.model.Movie
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_ITEM_NOT_SELECTED
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.MOVIE_ITEM_SELECTED

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val showShimmer = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .testTag(MOVIE_ITEM)
            .semantics{
                contentDescription = if(isSelected) MOVIE_ITEM_SELECTED + movie.title else MOVIE_ITEM_NOT_SELECTED + movie.title
            }
    ) {
        if (!isSelected || showShimmer.value) {
            AsyncImage(
                model = BASE_IMAGE_URL + movie.poster,
                contentDescription = movie.title,
                modifier = modifier
                    .testTag(REGULAR_IMAGE)
                    .background(
                        shimmerBrush(showShimmer = showShimmer.value),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (!showShimmer.value) Modifier.border(
                            1.dp,
                            DarkGrey,
                            RoundedCornerShape(12.dp)
                        )
                        else Modifier
                    )
                    .clickable {
                        onClick()
                    },
                contentScale = ContentScale.Crop,
                onSuccess = { showShimmer.value = false },
                onError = { showShimmer.value = false },
                error = painterResource(R.drawable.no_poster)
            )
        } else {
            CardWithAnimatedBorder(
                modifier = modifier
                    .testTag(IMAGE_WITH_ANIMATED_BORDER)
                    .clip(RoundedCornerShape(12.dp)),
                onCardClick = onClick
            ) {
                AsyncImage(
                    model = BASE_IMAGE_URL + movie.poster,
                    contentDescription = movie.title,
                    modifier = modifier
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    onSuccess = { showShimmer.value = false },
                    onError = { showShimmer.value = false },
                    error = painterResource(R.drawable.no_poster)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.title,
            fontFamily = fontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = LightGrey,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp
        )
        Text(
            text = if(movie.releaseDate.isEmpty()) "???" else movie.releaseDate.take(4),
            fontFamily = fontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun MovieItemPreview() {
    PopcornPicksTheme {
        MovieItem(
            movie = dummyMovie,
            isSelected = true,
            onClick = {}
        )
    }
}
