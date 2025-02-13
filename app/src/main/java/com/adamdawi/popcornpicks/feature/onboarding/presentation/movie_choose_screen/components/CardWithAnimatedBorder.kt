package com.adamdawi.popcornpicks.feature.onboarding.presentation.movie_choose_screen.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.adamdawi.popcornpicks.core.presentation.theme.DarkRed
import com.adamdawi.popcornpicks.core.presentation.theme.Red

@Composable
fun CardWithAnimatedBorder(
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    borderColors: List<Color> = listOf(
        Color.Black,
        DarkRed,
        Red,
        DarkRed,
        Color.Black
    ),
    animationDurationMillis: Int = 7000,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cardInfiniteTransition")
    val angle =
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec =
            infiniteRepeatable(
                animation = tween(animationDurationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "angleAnimation"
        )

    val brush =
        if (borderColors.isNotEmpty()) Brush.sweepGradient(borderColors)
        else Brush.sweepGradient(listOf(Color.Gray, Color.White))

    Surface(modifier = modifier.clickable { onCardClick() }, shape = RoundedCornerShape(12.dp)) {
        Surface(
            modifier =
            Modifier
                .clipToBounds()
                .fillMaxWidth()
                .padding(2.dp)
                .drawWithContent {
                    rotate(angle.value) {
                        drawCircle(
                            brush = brush,
                            radius = size.width,
                            blendMode = BlendMode.SrcIn,
                        )
                    }
                    drawContent()
                },
            color = Color.Black,
            shape = RoundedCornerShape(12.dp)
        ) {
            content()
        }
    }
}