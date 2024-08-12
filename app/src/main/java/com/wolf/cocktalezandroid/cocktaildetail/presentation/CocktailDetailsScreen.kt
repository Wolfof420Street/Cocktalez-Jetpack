package com.wolf.cocktalezandroid.cocktaildetail.presentation

import AppHeader
import CocktailObject
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wolf.cocktalezandroid.common.presentation.components.CocktailDivider
import com.wolf.cocktalezandroid.home.presentation.LoadingScreen


@OptIn(ExperimentalSharedTransitionApi::class)
@Destination<RootGraph>
@Composable
fun SharedTransitionScope.CocktailDetailsScreen(
    cocktailId: String,
    navigator: DestinationsNavigator,
    viewModel: CocktailDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getCocktailDetails(cocktailId)
    }

    val isFavorite by viewModel.isAFavorite(cocktailId.toInt()).collectAsState(initial = false)
    val uiState by viewModel.cocktailDetailsUiState.collectAsState()

    CocktailDetailsScreenContent(
        cocktail = uiState.cocktailDetails,
        isFavorite = isFavorite,
        state = uiState,
        onEvents = { event ->
            when (event) {
                is CocktailDetailsUIEvents.NavigateBack -> navigator.popBackStack()
                is CocktailDetailsUIEvents.AddToFavorites -> viewModel.insertFavorite(event.favorite)
                is CocktailDetailsUIEvents.RemoveFromFavorites -> viewModel.deleteFavorite(event.favorite)

            }
        }
    )
}


@Composable
fun CocktailDetailsScreenContent(
    cocktail: CocktailObject?,
    state: CocktailDetailsUiState,
    isFavorite: Boolean,
    onEvents: (CocktailDetailsUIEvents) -> Unit,
) {
    Scaffold(
        topBar = {
            AppHeader(
                onBackArrowClicked = {
                    onEvents(CocktailDetailsUIEvents.NavigateBack)
                },
                modifier = Modifier.fillMaxWidth(),
                showBackArrow = true
            )
        },
        content = { innerPadding ->
            if (state.isLoading) {
                LoadingScreen()
            } else if (state.error != null) {
                Text(
                    text = state.error,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    textAlign = TextAlign.Center
                )
            } else {
                cocktail?.let {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            ImageBtn(data = cocktail)
                        }
                        item {
                            InfoColumn(data = cocktail)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ImageBtn(data: CocktailObject) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(data.strDrinkThumb)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clickable { showDialog = false }
                )
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .clickable { showDialog = true }
            .padding(vertical = 8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(data.strDrinkThumb)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}



@Composable
fun InfoColumn(data: CocktailObject) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        if (data.strCategory.isNotEmpty()) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(durationMillis = 600, delayMillis = 150))
            ) {
                Text(
                    text = data.strCategory.uppercase(),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(durationMillis = 600, delayMillis = 250))
        ) {
            Text(
                text = data.strDrink,
                textAlign = TextAlign.Center,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(durationMillis = 600, delayMillis = 500))
        ) {
            CocktailDivider(isExpanded = false, duration = 600)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            listOf(
                InfoRow(data.strIngredient1 ?: "", data.strMeasure1),
                InfoRow(data.strIngredient2 ?: "", data.strMeasure2),
                InfoRow(data.strIngredient3 ?: "", data.strMeasure3),
                InfoRow(data.strIngredient4 ?: "", data.strMeasure4 ?: ""),
                InfoRow(data.strIngredient5 ?: "", data.strMeasure5 ?: ""),
                InfoRow(data.strIngredient6 ?: "", data.strMeasure6 ?: "")
            ).forEachIndexed { index, row ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(durationMillis = 600, delayMillis = 600 + index * 100))
                        .plus(slideInHorizontally(initialOffsetX = { it / 5 }))
                ) {
                    row
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(durationMillis = 1500))
                .plus(slideInHorizontally(initialOffsetX = { it / 5 }))
        ) {
            Text(
                text = data.strInstructions,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    if (value.isEmpty()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label.uppercase(),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value.ifEmpty { "--" },
            modifier = Modifier.weight(0.6f)
        )
    }
}
