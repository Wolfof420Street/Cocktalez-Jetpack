package com.wolf.cocktalezandroid.common.presentation.components

import android.graphics.drawable.shapes.OvalShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun AppPageIndicator(
    count: Int,
    pagerState: PagerState,
    onDotClicked: ((Int) -> Unit)? = null,
    color: Color = Color.Magenta,
    dotSize: Float = 6f,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .background(Color.Transparent)
            .semantics {
                contentDescription = "${(pagerState.currentPage % count) + 1}"
                liveRegion = LiveRegionMode.Polite
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.wrapContentHeight().align(Alignment.BottomCenter).padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(count) { iteration ->
                val currentColor = if (pagerState.currentPage == iteration) color else color.copy(alpha = 0.5f)
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(currentColor)
                        .size(dotSize.dp)
                        .clickable {
                            onDotClicked?.invoke(iteration)
                        }
                )
            }
        }
    }
}
