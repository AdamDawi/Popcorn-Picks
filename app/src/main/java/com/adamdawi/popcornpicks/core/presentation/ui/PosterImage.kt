package com.adamdawi.popcornpicks.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.domain.util.Constants
import com.adamdawi.popcornpicks.core.presentation.theme.Grey
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme

@Composable
fun PosterImage(
    modifier: Modifier = Modifier,
    posterUrl: String
) {
    val showShimmer = remember { mutableStateOf(true) }
    AsyncImage(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, Grey, RoundedCornerShape(6.dp))
            .background(shimmerBrush(showShimmer = showShimmer.value)),
        model = Constants.Network.BASE_IMAGE_URL + posterUrl,
        contentDescription = "Movie poster",
        error = painterResource(R.drawable.no_poster),
        onError = {showShimmer.value = false},
        onSuccess = {showShimmer.value = false},
        contentScale = ContentScale.Crop
    )
}

@Preview
@Composable
private fun PosterImagePreview() {
    PopcornPicksTheme {
        PosterImage(
            modifier = Modifier
                .width(140.dp)
                .height(220.dp),
            posterUrl = "d"
        )
    }
}