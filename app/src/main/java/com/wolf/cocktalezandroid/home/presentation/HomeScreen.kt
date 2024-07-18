package com.wolf.cocktalezandroid.home.presentation

import CocktailObject
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wolf.cocktalezandroid.common.presentation.theme.PrimaryColor
import kotlin.math.absoluteValue

@OptIn(ExperimentalSharedTransitionApi::class)
@Destination<RootGraph>(start = true)
@Composable

fun SharedTransitionScope.HomeScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigator: DestinationsNavigator,
    viewModel: CocktailViewModel = hiltViewModel()
) {
    val cocktailUiState by viewModel.cocktailUiState.collectAsState()
    val context = LocalContext.current



}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun CocktailCardRenderer(
    offset: Float,
    cardWidth: Dp = 250.dp,
    cardHeight: Dp,
    cocktail: CocktailObject
) {
    val maxParallax = 30
    val globalOffset = offset * maxParallax * 2

    Box(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .padding(top = 8.dp)
    ) {
        // Card background color & decoration
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            color = PrimaryColor,
            shadowElevation = calculateElevation(offset)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // City image, out of card by 15px
                AsyncImage(
                    model = rememberAsyncImagePainter(cocktail.strDrinkThumb),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(cardWidth - 24.dp, cardHeight - 24.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.size(8.dp))
                // City information
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = cocktail.strDrink,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

fun calculateElevation(offset: Float): Dp {
    val baseElevation = 4.dp
    val maxElevation = 10.dp
    return baseElevation + (maxElevation - baseElevation) * (offset.absoluteValue / 1f)
}

@Composable
fun Rotation3d(
    rotationX: Float = 0f,
    rotationY: Float = 0f,
    rotationZ: Float = 0f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.graphicsLayer(
            rotationX = rotationX,
            rotationY = rotationY,
            rotationZ = rotationZ
        )
    ) {
        content()
    }
}