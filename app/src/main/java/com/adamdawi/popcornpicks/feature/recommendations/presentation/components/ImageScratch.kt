package com.adamdawi.popcornpicks.feature.recommendations.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.adamdawi.popcornpicks.R
import kotlin.math.pow
import kotlin.math.sqrt


@Composable
fun ImageScratch(
    modifier: Modifier = Modifier,
    overlayImage: ImageBitmap,
    baseImage: Painter,
    scratchingThreshold : Float = 0.8f
) {
    val lines = remember {
        mutableStateListOf<Line>()
    }
    val density = LocalDensity.current
    val totalScratchedArea = remember {
        mutableFloatStateOf(0f)
    }
    Box(
        modifier
            .fillMaxSize()
    ){
        Image(
            painter = baseImage,
            contentDescription = "Overlay",
            modifier = Modifier
                .fillMaxSize()
        )
        Box(
            modifier = Modifier
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                },
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(true) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()

                            val line = Line(
                                start = change.position - dragAmount,
                                end = change.position
                            )

                            lines.add(line)
                            totalScratchedArea.floatValue = calculateTotalArea(lines.toList(), density)
                        }
                    }
            ) {
                val imageSize = IntSize(width = size.width.toInt(), height = size.height.toInt())
                val maxCanvasArea = this.size.width.toFloat() * this.size.height.toFloat()

                //if total scratched area is less than scratching threshold then show overlay image
                if(totalScratchedArea.floatValue/maxCanvasArea < scratchingThreshold) {
                    drawImage(overlayImage, dstSize = imageSize)
                }
                lines.forEach { line ->
                    drawLine(
                        color = line.color,
                        start = line.start,
                        end = line.end,
                        strokeWidth = line.strokeWidth.toPx(),
                        cap = StrokeCap.Round,
                        blendMode = BlendMode.Clear
                    )
                }
            }
        }
    }
}

data class Line(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Transparent,
    val strokeWidth: Dp = 40.dp
)

fun calculateTotalArea(lines: List<Line>, density: Density): Float {
    var sum = 0f
    lines.forEach { line ->
        val length = distance(line.start, line.end)
        val lineWidth = with(density) { line.strokeWidth.toPx() }
        sum += length * lineWidth
    }
    return sum
}

fun distance(start: Offset, end: Offset): Float {
    return sqrt((end.x - start.x).pow(2) + (end.y - start.y).pow(2))
}

@Composable
@Preview
fun ImageScratchPreview() {
    ImageScratch(
        overlayImage = ImageBitmap.imageResource(R.drawable.overlay),
        baseImage = painterResource(R.drawable.poster)
    )
}