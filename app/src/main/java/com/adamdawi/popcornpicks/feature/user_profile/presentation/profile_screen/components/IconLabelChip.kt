package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.presentation.theme.Blue
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme

@Composable
fun IconLabelChip(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    contentDescription: String?
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(Blue.copy(alpha = .4f))
            .border(1.dp, Blue.copy(alpha = .5f), CircleShape)
            .padding(20.dp)
    ) {
        Icon(
            modifier = Modifier
                .fillMaxHeight()

                .aspectRatio(1f),
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}


@Preview
@Composable
private fun IconLabelChipPreview() {
    PopcornPicksTheme {
        IconLabelChip(
            modifier = Modifier
                .width(300.dp)
                .height(150.dp),
            icon = R.drawable.pen_ic,
            contentDescription = null
        )
    }
}