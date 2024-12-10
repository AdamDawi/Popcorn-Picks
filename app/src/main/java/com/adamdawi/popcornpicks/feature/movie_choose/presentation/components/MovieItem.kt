package com.adamdawi.popcornpicks.feature.movie_choose.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adamdawi.popcornpicks.core.theme.Blue
import com.adamdawi.popcornpicks.core.theme.DarkGrey
import com.adamdawi.popcornpicks.core.theme.LightGrey
import com.adamdawi.popcornpicks.core.theme.fontFamily
import com.adamdawi.popcornpicks.core.ui.shimmerBrush
import com.adamdawi.popcornpicks.core.utils.Constants.Network.BASE_IMAGE_URL
import com.adamdawi.popcornpicks.feature.movie_choose.domain.Movie

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val showShimmer = remember { mutableStateOf(true) }
    Column {
        if (!isSelected || showShimmer.value) {
            AsyncImage(
                model = BASE_IMAGE_URL + movie.poster,
                contentDescription = movie.title,
                modifier = modifier
                    .background(shimmerBrush(showShimmer = showShimmer.value), shape = RoundedCornerShape(12.dp))
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (!showShimmer.value) Modifier.border(1.dp, DarkGrey, RoundedCornerShape(12.dp))
                        else Modifier
                    )
                    .clickable {
                        onClick()
                    },
                contentScale = ContentScale.Crop,
                onSuccess = { showShimmer.value = false }
            )
        } else {
            CardWithAnimatedBorder(
                modifier = modifier
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
                    onSuccess = { showShimmer.value = false }
                )
            }
        }
        Text(
            text = movie.title,
            fontFamily = fontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = LightGrey,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = movie.releaseDate.take(4),
            fontFamily = fontFamily,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
