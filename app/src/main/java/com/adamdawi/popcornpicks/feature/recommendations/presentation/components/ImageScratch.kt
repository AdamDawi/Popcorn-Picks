package com.adamdawi.popcornpicks.feature.recommendations.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.adamdawi.popcornpicks.R

@Composable
fun ImageScratch(
    modifier: Modifier = Modifier,
    overlayImage: ImageBitmap,
    baseImage: ImageBitmap,
    isIntersect: Boolean = true,
    startPadding: Dp = 16.dp,
    endPadding: Dp = 16.dp,
) {
    val currentPathState = remember { mutableStateOf(Path()) }
    val movedOffsetState = remember { mutableStateOf<Offset?>(null) }
    val revealedPercentage = remember { mutableStateOf(0f) }
    val isFullyRevealed = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(start = startPadding, end = endPadding),
    ) {
        var clipOp = ClipOp.Intersect
        Canvas(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(size = 6.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        movedOffsetState.value = change.position
                    }
                },
        ) {
            val imageSize = IntSize(width = size.width.toInt(), height = size.height.toInt())

            // Draw the overlay image
            drawImage(overlayImage, dstSize = imageSize)

            // Update the path when user drags
            movedOffsetState.value?.let {
                currentPathState.value.addOval(Rect(it, 50f))
            }

            // Check if clipping mode should be switched
            if (!isIntersect) {
                clipOp = ClipOp.Difference
            }

            // Calculate revealed percentage
            val revealedArea = currentPathState.value.getBounds().area()
            val totalArea = imageSize.width * imageSize.height.toFloat()
            revealedPercentage.value = (revealedArea / totalArea) * 100

            if (revealedPercentage.value >= 80f) {
                isFullyRevealed.value = true
            }

            // Reveal the base image
            if (isFullyRevealed.value) {
                drawImage(
                    image = baseImage,
                    dstSize = imageSize
                )
            } else {
                clipPath(path = currentPathState.value, clipOp = clipOp) {
                    drawImage(baseImage, dstSize = imageSize)
                }
            }
        }
    }
}

private fun Rect.area(): Float {
    return width * height
}


private data class DraggedPath(
    val path: Path,
    val width: Float = 18f
)

@Composable
@Preview()
fun ImageScratchPreview() {
    ImageScratch(
        overlayImage = ImageBitmap.imageResource(R.drawable.overlay),
        baseImage = ImageBitmap.imageResource(R.drawable.poster)
    )
}