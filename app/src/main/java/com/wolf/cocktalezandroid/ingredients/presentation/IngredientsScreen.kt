package com.wolf.cocktalezandroid.ingredients.presentation

import AppHeader
import Ingredient
import android.content.Context
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wolf.cocktalezAndroid.R
import com.wolf.cocktalezandroid.home.presentation.LoadingScreen
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.abs


@Destination<RootGraph>
@Composable
fun IngredientsScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigator: DestinationsNavigator,
    viewModel: IngredientsViewModel = hiltViewModel()
) {
    val ingredientsUiState by viewModel.ingredientsUIState.collectAsState()
    val context = LocalContext.current

    IngredientsScreenContent(
        state = ingredientsUiState,
        animatedVisibilityScope = animatedVisibilityScope,
        context = context,
        viewModel = viewModel,
        onEvent = { event ->
            when (event) {
                is IngredientsScreenUiEvents.OnIngredientTapped -> {
                    // Handle ingredient tap
                }
                is IngredientsScreenUiEvents.PageChanged -> {
                    viewModel.onPageChanged(event.pageIndex)
                }
                IngredientsScreenUiEvents.PullToRefresh -> {
                    viewModel.refresh()
                }
                IngredientsScreenUiEvents.NavigateBack -> {
                    navigator.navigateUp()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsScreenContent(
    state: IngredientsUIState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onEvent: (IngredientsScreenUiEvents) -> Unit,
    context: Context,
    viewModel: IngredientsViewModel
) {
    val ingredients = state.ingredients.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(
        initialPage = state.pageIndex,
        pageCount = { ingredients.itemCount }
    )
    val (shortMode, bottomHeight) = calculateShortModeAndBottomHeight()

    val itemHeight = (LocalConfiguration.current.screenHeightDp.dp - 250.dp).coerceIn(250.dp, 400.dp)
    val itemWidth = itemHeight * 0.666f

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onEvent(IngredientsScreenUiEvents.PageChanged(page))
        }
    }

    Scaffold(
        topBar = {
            AppHeader(
                onBackArrowClicked = {
                    onEvent(IngredientsScreenUiEvents.NavigateBack)
                },
                title = { Text(text = context.getString(R.string.ingredient_header)) },
                modifier = Modifier.fillMaxWidth(),
                showBackArrow = false,
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            isRefreshing = false,
            onRefresh = {
                onEvent(IngredientsScreenUiEvents.PullToRefresh)
            }
        ) {
            when {
                ingredients.loadState.refresh is LoadState.Loading -> {
                    LoadingScreen()
                }
                ingredients.loadState.refresh is LoadState.NotLoading && ingredients.itemCount < 1 -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = "No data available",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
                ingredients.loadState.refresh is LoadState.Error -> {
                    val errorMessage = when ((ingredients.loadState.refresh as LoadState.Error).error) {
                        is HttpException -> "Oops, something went wrong!"
                        is IOException -> "Couldn't reach server, check your internet connection!"
                        else -> "Unknown error occurred"
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {

                        // Blurred Background
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            ingredients[state.pageIndex]?.let { ingredient ->
                                BlurredImageBg(
                                    url = viewModel.generateIngredientImageUrl(ingredient.strIngredient),
                                    useBlurs = true
                                )
                            }
                        }

                        // Circle Background
                        BgCircle()

                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(itemHeight)  // Set a reasonable height to avoid cropping
                                    .padding(vertical = 16.dp),
                                pageSize = PageSize.Fixed(itemWidth),
                                pageSpacing = 16.dp // Spacing between pages
                            ) { pageIndex ->
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    val wrappedIndex = pageIndex % ingredients.itemCount

                                    val ingredient = ingredients[wrappedIndex] ?: return@HorizontalPager

                                    CollapsingCarouselItem(
                                        content = {
                                            DoubleBorderImage(
                                                modifier = Modifier.size(itemWidth),
                                                imageUrl = viewModel.generateIngredientImageUrl(ingredient.strIngredient)
                                            )
                                        },
                                        indexOffset = pagerState.currentPage - wrappedIndex,
                                        width = itemWidth,
                                        onPressed = {},
                                        title = ingredient.strIngredient
                                    )
                                }
                            }
                        }

                        // Bottom Text Content near the bottom of the screen
                        ingredients[state.pageIndex]?.let {
                            // Bottom Text
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .height(bottomHeight)
                                    .padding(top = 60.dp)
                            ) {
                                BottomTextContent(
                                    ingredient = it,
                                    onIngredientTap = {},
                                    currentPage = state.pageIndex,
                                    pageCount = ingredients.itemCount,
                                    shortMode = shortMode
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomTextContent(
    ingredient: Ingredient,
    onIngredientTap: (Int) -> Unit,
    currentPage: Int,
    pageCount: Int,
    shortMode: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .padding(bottom = 32.dp),  // Add padding to position it just above the bottom of the screen
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom // Align content at the bottom of the screen
    ) {
        Text(
            text = ingredient.strIngredient,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.Black,
                lineHeight = 1.2.em
            ),
            textAlign = TextAlign.Center,
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {
                    this.role = Role.Button
                    this.onClick {
                        onIngredientTap(currentPage)
                        true
                    }
                }
        )

        if (!shortMode) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "", // Placeholder for additional text, replace as needed
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!shortMode) {
            PageIndicator(
                currentPage = currentPage,
                pageCount = pageCount,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onIngredientTap(currentPage) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Cocktails")
        }

        Spacer(modifier = Modifier.height(16.dp)) // Keeps the button above the bottom of the screen
    }
}

@Composable
fun BgCircle() {
    val size = 2000.dp
    Box(
        modifier = Modifier
            .size(size)
            .offset(y = size * 0.2f) // Positioning the circle higher
            .background(
                color = Color.White.copy(alpha = 0.8f),
                shape = RoundedCornerShape(topStart = size, topEnd = size)
            )
    )
}

@Composable
fun calculateShortModeAndBottomHeight(): Pair<Boolean, Dp> {
    val configuration = LocalConfiguration.current
    val screenHeightPx = configuration.screenHeightDp.dp.value

    val shortMode = screenHeightPx <= 800
    val bottomHeight = screenHeightPx / 2.75

    return remember {
        Pair(shortMode, bottomHeight.dp)
    }
}


@Composable
fun PageIndicator(
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until pageCount) {
            Box(
                modifier = Modifier
                    .size(if (i == currentPage) 12.dp else 8.dp)
                    .padding(2.dp)
                    .background(
                        color = if (i == currentPage) Color.Yellow else Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }
}


@Composable
fun BlurredImageBg(
    url: String?,
    useBlurs: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cardColor = MaterialTheme.colorScheme.surface

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(cardColor)
            .graphicsLayer(
                scaleX = 1.25f,
                scaleY = 1.25f,
                translationY = -0.2f * LocalConfiguration.current.screenHeightDp.dp.value // Adjusted to move higher
            )
    ) {
        if (url != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (useBlurs) {
                            Modifier.blur(6.dp)
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}


@Composable
fun CollapsingCarouselItem(
    content: @Composable () -> Unit,
    indexOffset: Int,
    width: Dp,
    onPressed: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    val tallHeight = width * 1.5f  // Height based on aspect ratio
    val vtOffset = when {
        indexOffset == 1 -> width * 0.5f
        indexOffset == 2 -> width * 0.825f
        indexOffset > 2 -> width
        else -> 0.dp
    }

    val animatedOpacity by animateFloatAsState(
        label = title,
        targetValue = if (abs(indexOffset) <= 2) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    // Adjust the offset to prevent cropping
    val animatedOffset by animateDpAsState(
        label = title,
        targetValue = if (indexOffset == 0) 0.dp else vtOffset,  // Use vtOffset directly to prevent negative movement
        animationSpec = tween(durationMillis = 300)
    )

    val animatedHeight by animateDpAsState(
        label = title,
        targetValue = if (indexOffset == 0) tallHeight else width,
        animationSpec = tween(durationMillis = 300)
    )

    val animatedPadding by animateDpAsState(
        label = title,
        targetValue = if (indexOffset == 0) 0.dp else width * 0.1f,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = modifier
            .alpha(animatedOpacity)
            .offset { IntOffset(x = 0, y = animatedOffset.roundToPx()) }  // Use the adjusted offset
            .height(animatedHeight)  // Ensure the item height is correct
            .width(width)
            .padding(animatedPadding)
            .clickable(
                enabled = indexOffset <= 2,
                onClick = onPressed,
                onClickLabel = title
            )
    ) {
        content()
    }
}


@Composable
fun DoubleBorderImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .border(1.dp, Color.White, RoundedCornerShape(50.dp))
            .padding(4.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color.Gray)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
