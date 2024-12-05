package com.adamdawi.popcornpicks.feature.recommendations.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
    scratchingThreshold : Float = 0.8f,
    scratchLineWidth : Dp = 32.dp,
    scratchLineCap : StrokeCap = StrokeCap.Round
) {
    val scratchLines = remember {
        mutableStateListOf<Line>()
    }
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
                }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(true) {
                        detectTapGestures(
                            onTap = { position ->
                                val line = Line(
                                    start = position,
                                    end = position,
                                    strokeWidth = scratchLineWidth.toPx()
                                )
                                //accumulate the total scratched area, including overlapping lines
                                totalScratchedArea.floatValue += line.calculateArea()
                                scratchLines.add(line)
                            }
                        )
                    }
                    .pointerInput(true) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()

                            val line = Line(
                                start = change.position - dragAmount,
                                end = change.position,
                                strokeWidth = scratchLineWidth.toPx()
                            )
                            //accumulate the total scratched area, including overlapping lines
                            totalScratchedArea.floatValue += line.calculateArea()

                            scratchLines.add(line)
                        }
                    }
            ) {
                val imageSize = IntSize(width = size.width.toInt(), height = size.height.toInt())
                val maxCanvasArea = this.size.width.toFloat() * this.size.height.toFloat()

                //if total scratched area is below the threshold, show the overlay image
                if(totalScratchedArea.floatValue/maxCanvasArea < scratchingThreshold) {
                    drawImage(image = overlayImage, dstSize = imageSize)
                }

                //draw the scratch lines with transparency to "erase" the overlay
                scratchLines.forEach { line ->
                    drawLine(
                        color = Color.Transparent,
                        start = line.start,
                        end = line.end,
                        strokeWidth = line.strokeWidth,
                        cap = scratchLineCap,
                        blendMode = BlendMode.Clear
                    )
                }
            }
        }
    }
}

private data class Line(
    val start: Offset,
    val end: Offset,
    val strokeWidth: Float
){
    fun calculateArea(): Float {
        val lineLength = calculateLength()
        return lineLength * strokeWidth
    }
    private fun calculateLength(): Float {
        return sqrt((end.x - start.x).pow(2) + (end.y - start.y).pow(2))
    }
}

@Composable
@Preview
fun ImageScratchPreview() {
    ImageScratch(
        overlayImage = ImageBitmap.imageResource(R.drawable.popcorn_overlay3),
        baseImage = painterResource(R.drawable.poster)
    )
}