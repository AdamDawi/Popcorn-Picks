package com.adamdawi.popcornpicks.feature.movie_choose.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adamdawi.popcornpicks.core.theme.LightGrey
import com.adamdawi.popcornpicks.core.theme.Red
import com.adamdawi.popcornpicks.core.theme.fontFamily
import com.adamdawi.popcornpicks.core.utils.Constants.Network.BASE_URL_IMAGE
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
                model = BASE_URL_IMAGE + movie.poster,
                contentDescription = movie.title,
                modifier = modifier
                    .background(shimmerBrush(showShimmer = true), shape = RoundedCornerShape(12.dp))
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .then(
                        if (!showShimmer.value) Modifier.border(1.dp, Gray, RoundedCornerShape(12.dp))
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
                    .background(shimmerBrush(showShimmer = true), shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp)),
                onCardClick = onClick
            ) {
                AsyncImage(
                    model = BASE_URL_IMAGE + movie.poster,
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
            color = Red,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1300f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Gray.copy(alpha = 0.3f),
            Gray.copy(alpha = 0.1f),
            Gray.copy(alpha = 0.3f)
        )

        val transition = rememberInfiniteTransition()
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            )
        )

        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}
