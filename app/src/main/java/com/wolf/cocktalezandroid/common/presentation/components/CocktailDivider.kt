package com.wolf.cocktalezandroid.common.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wolf.cocktalezAndroid.R

@Composable
fun CocktailDivider(
    isExpanded: Boolean,
    duration: Int = 1500, // Duration in milliseconds
    linesColor: Color = MaterialTheme.colorScheme.primary,
    compassColor: Color = MaterialTheme.colorScheme.onSecondary
) {
    var animatedValue by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isExpanded) {
        animatedValue = if (isExpanded) 1f else 0f
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedDivider(
            modifier = Modifier.weight(1f),
            isExpanded = isExpanded,
            duration = duration,
            linesColor = linesColor
        )

        Spacer(modifier = Modifier.width(8.dp))

        AnimatedCompassIcon(
            isExpanded = isExpanded,
            duration = duration,
            compassColor = compassColor
        )

        Spacer(modifier = Modifier.width(8.dp))

        AnimatedDivider(
            modifier = Modifier.weight(1f),
            isExpanded = isExpanded,
            duration = duration,
            linesColor = linesColor,
            alignLeft = true
        )
    }
}

@Composable
fun AnimatedDivider(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    duration: Int,
    linesColor: Color,
    alignLeft: Boolean = false
) {
    val transitionState = remember { androidx.compose.animation.core.Animatable(0f) }
    val targetValue = if (isExpanded) 1f else 0f

    LaunchedEffect(isExpanded) {
        transitionState.animateTo(targetValue, animationSpec = androidx.compose.animation.core.tween(durationMillis = duration))
    }

    HorizontalDivider(
        modifier = modifier
            .graphicsLayer(
                scaleX = transitionState.value,
                scaleY = 1f,
                transformOrigin = if (alignLeft) {
                    TransformOrigin(0f, 0.5f)
                } else {
                    TransformOrigin(1f, 0.5f)
                }
            ),
        thickness = 0.5.dp,
        color = linesColor
    )
}

@Composable
fun AnimatedCompassIcon(
    isExpanded: Boolean,
    duration: Int,
    compassColor: Color
) {
    val transitionState = remember { androidx.compose.animation.core.Animatable(0f) }
    val targetValue = if (isExpanded) 0.5f else 0f

    LaunchedEffect(isExpanded) {
        transitionState.animateTo(targetValue, animationSpec = androidx.compose.animation.core.tween(durationMillis = duration))
    }

    Image(
        painter = painterResource(id = R.drawable.compass_full),
        contentDescription = null,
        modifier = Modifier
            .size(32.dp)
            .background(compassColor)
            .graphicsLayer(rotationZ = transitionState.value * 360),
        contentScale = ContentScale.Fit
    )
}
