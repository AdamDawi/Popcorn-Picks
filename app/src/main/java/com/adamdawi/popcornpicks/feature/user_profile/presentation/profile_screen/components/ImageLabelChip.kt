package com.adamdawi.popcornpicks.feature.user_profile.presentation.profile_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamdawi.popcornpicks.R
import com.adamdawi.popcornpicks.core.domain.util.Constants.Tests.ICON_LABEL_CHIP
import com.adamdawi.popcornpicks.core.presentation.theme.Blue
import com.adamdawi.popcornpicks.core.presentation.theme.PopcornPicksTheme

@Composable
fun ImageLabelChip(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int,
    contentDescription: String? = null,
    label: String
) {
    Row(
        modifier = modifier
            .testTag(ICON_LABEL_CHIP)
            .clip(CircleShape)
            .background(Blue.copy(alpha = .4f))
            .border(1.dp, Blue.copy(alpha = .5f), CircleShape)
            .padding(8.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(25.dp),
            painter = painterResource(imageId),
            contentDescription = contentDescription
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
private fun IconLabelChipPreview() {
    PopcornPicksTheme {
        ImageLabelChip(
            modifier = Modifier
                .width(150.dp)
                .height(50.dp),
            imageId = R.drawable.horror,
            label = "Horror"
        )
    }
}