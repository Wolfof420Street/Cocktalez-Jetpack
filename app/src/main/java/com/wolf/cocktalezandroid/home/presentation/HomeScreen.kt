package com.wolf.cocktalezandroid.home.presentation

import AppHeader
import CocktailObject
import Drinks
import android.content.Context
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CocktailDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wolf.cocktalezAndroid.R
import com.wolf.cocktalezandroid.common.presentation.theme.CardDarkColor
import com.wolf.cocktalezandroid.common.presentation.theme.CardLightColor
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.PI
import kotlin.math.abs


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


    HomeScreenContent(
        state = cocktailUiState,
        animatedVisibilityScope = animatedVisibilityScope,
        context = context,
        onEvent = { event -> when(event) {
            CocktailUiEvents.NavigateBack -> {
                navigator.navigateUp()
            }
            is CocktailUiEvents.NavigateToCocktailDetails -> {
                navigator.navigate(
                    CocktailDetailsScreenDestination(
                        cocktailId = event.cocktail.idDrink
                    )
                )
            }
            CocktailUiEvents.OnPullToRefresh -> {
                viewModel.refreshAllData()
            }
            is CocktailUiEvents.OnTabSelected -> {
                 viewModel.onTabSelected(event.tabIndex)
            }
            is CocktailUiEvents.OnSearchQueried -> {
                viewModel.onSearchQueried(event.query)
            }
        } }
    )


}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
fun SharedTransitionScope.HomeScreenContent(
    state: CocktailUiState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onEvent: (CocktailUiEvents) -> Unit,
    context : Context
) {

    val selectedTabIndex = state.selectedTabIndex

    val searchQuery = state.searchQuery


    Scaffold(
        topBar = {
            AppHeader(
                onBackArrowClicked = {
                    onEvent(CocktailUiEvents.NavigateBack)
                },
                title = { Text(text = context.getString(R.string.app_header,
                    ),
                style = MaterialTheme.typography.bodyLarge)},
                modifier = Modifier.fillMaxWidth(),
                showBackArrow = false,
            )
        }
    ) {
    innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            isRefreshing = false,
            onRefresh = {
                onEvent(CocktailUiEvents.OnPullToRefresh)
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp,
                        vertical = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SearchBar(
                        searchQuery = searchQuery,
                        onSearchQueryChanged = {
                            onEvent(CocktailUiEvents.OnSearchQueried(searchQuery))
                        }
                    )

                }

                item {
                    TabbedContent(
                        state = state,
                        onTabSelected = {
                            onEvent(CocktailUiEvents.OnTabSelected(selectedTabIndex
                            ))
                        },
                        onClick = {
                            onEvent(CocktailUiEvents.NavigateToCocktailDetails(it))
                        },
                        selectedTabIndex = selectedTabIndex,
                        animatedVisibilityScope = animatedVisibilityScope
                        )
                }
            }
        }
    }
}


@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        placeholder = { Text("Search for Cocktails", color = Color.Gray) }, // Adjust color as needed
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        shape = RoundedCornerShape(30.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null,
            tint = Color.Gray) }, // Adjust icon color

    )

}


@Composable
fun TabbedContent(
    state: CocktailUiState,
    onTabSelected: (Int) -> Unit,
    onClick: (Drinks) -> Unit,
    selectedTabIndex: Int,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val tabItems = listOf("Popular", "Alcoholic", "Mocktails")

    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
        pageCount = { tabItems.size }
    )

    val coroutineScope = rememberCoroutineScope()

    val alcoholicCocktails = state.alcoholicCocktails.collectAsLazyPagingItems()
    val popularCocktails = state.popularCocktails.collectAsLazyPagingItems()
    val nonAlcoholicCocktails = state.nonAlcoholicCocktails.collectAsLazyPagingItems()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val cardHeight = (screenHeight * 0.48f).coerceIn(300.dp, 400.dp)
    val cardWidth = cardHeight * 0.8f

    var normalizedOffset by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPageOffsetFraction }
            .collect { offset ->
                normalizedOffset = offset
            }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onTabSelected(page)
        }
    }

    Column {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage])
                )
            }
        ) {
            tabItems.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        }
        HorizontalPager(
            state = pagerState
        ) { page ->
            when (page) {
                0 -> PopularCocktails(
                    cardHeight = cardHeight,
                    cardWidth = cardWidth,
                    items = popularCocktails,
                    onClick = onClick,
                    animatedVisibilityScope = animatedVisibilityScope
                )
                1 -> AlcoholicCocktails(
                    items = alcoholicCocktails,
                    onClick = onClick,
                    cardHeight = cardHeight,
                    cardWidth = cardWidth,
                    animatedVisibilityScope = animatedVisibilityScope
                )
                2 -> NonAlcoholicCocktails(
                    cardHeight = cardHeight,
                    cardWidth = cardWidth,
                    items = nonAlcoholicCocktails,
                    onClick = onClick,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }
        }
    }
}

@Composable
fun NonAlcoholicCocktails(
    items: LazyPagingItems<Drinks>,
    onClick: (Drinks) -> Unit,
    cardHeight: Dp,
    cardWidth: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    CardRow(
        items = items,
        cardHeight = cardHeight,
        cardWidth = cardWidth,
        content = { item, normalizedOffset ->
            Rotation3d(
                rotationX = 0f,
                rotationY = normalizedOffset * 20f,
                rotationZ = 0f
            ) {
                CocktailCardRenderer(
                    offset = normalizedOffset,
                    cocktail = item,
                    cardWidth = cardWidth,
                    cardHeight = cardHeight,
                    onClick = onClick
                )
            }
        }
    )
}

@Composable
fun AlcoholicCocktails(
    items: LazyPagingItems<Drinks>,
    onClick: (Drinks) -> Unit,
    cardHeight: Dp,
    cardWidth: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    CardRow(
        items = items,
        cardHeight = cardHeight,
        cardWidth = cardWidth,
        content = { item, normalizedOffset ->
            Rotation3d(
                rotationX = 0f,
                rotationY = normalizedOffset * 20f,
                rotationZ = 0f
            ) {
                CocktailCardRenderer(
                    offset = normalizedOffset,
                    cocktail = item,
                    cardWidth = cardWidth,
                    cardHeight = cardHeight,
                    onClick = onClick
                )
            }
        }
    )
}

@Composable
fun PopularCocktails(
    items: LazyPagingItems<CocktailObject>,
    onClick: (Drinks) -> Unit,
    cardHeight: Dp,
    cardWidth: Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    CardRow(
        items = items,
        cardHeight = cardHeight,
        cardWidth = cardWidth,
        content = { item, normalizedOffset ->
            Rotation3d(
                rotationX = 0f,
                rotationY = normalizedOffset * 20f,
                rotationZ = 0f
            ) {
                CocktailCardRenderer(
                    offset = normalizedOffset,
                    cocktail = Drinks(
                        strDrink = item.strDrink,
                        strDrinkThumb = item.strDrinkThumb,
                        idDrink = item.idDrink
                    ),
                    cardWidth = cardWidth,
                    cardHeight = cardHeight,
                    onClick = onClick
                )
            }
        }
    )
}



@Composable
fun <T : Any> CardRow(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<T>,
    cardHeight: Dp,
    cardWidth: Dp,
    maxRotation: Float = 20f,
    content: @Composable (T, Float) -> Unit,
) {
    val pagerState = rememberPagerState(
        pageCount = { items.itemCount }
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(cardHeight + 60.dp), // Adjusted height to fit cards and parallax effect
        contentAlignment = Alignment.Center
    ) {
        if (items.itemCount > 0) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp), // Padding to add space on edges
                pageSize = PageSize.Fixed(cardWidth),
                beyondViewportPageCount = 3,
                pageSpacing = 16.dp // Space between items
            ) { page ->

                val normalizedOffset = ((pagerState.currentPage - page) +
                        pagerState.currentPageOffsetFraction)

                // Scale the card based on the offset
                val scale = 1f - 0.15f * abs(normalizedOffset)

                val item = items[page]
                if (item != null) {
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                alpha = scale
                            }
                            .padding(horizontal = 8.dp)
                    ) {
                        content(item, normalizedOffset)
                    }
                }
            }
        }

        items.loadState.let { loadState ->
            when {
                loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                loadState.refresh is LoadState.NotLoading && items.itemCount < 1 -> {
                    Text(
                        text = "No data available",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                loadState.refresh is LoadState.Error -> {
                    Text(
                        text = when ((loadState.refresh as LoadState.Error).error) {
                            is HttpException -> {
                                "Oops, something went wrong!"
                            }
                            is IOException -> {
                                "Couldn't reach server, check your internet connection!"
                            }
                            else -> {
                                "Unknown error occurred"
                            }
                        },
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                loadState.append is LoadState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                loadState.append is LoadState.Error -> {
                    Text(
                        text = "An error occurred",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}



@Composable
fun Rotation3d(
    rotationX: Float = 0f,
    rotationY: Float = 0f,
    rotationZ: Float = 0f,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val degreesToRadians = PI / 180f
    val rotationXInRadians = rotationX * degreesToRadians.toFloat()
    val rotationYInRadians = rotationY * degreesToRadians.toFloat()
    val rotationZInRadians = rotationZ * degreesToRadians.toFloat()

    Box(
        modifier = modifier.graphicsLayer {
            // Set perspective to create a 3D effect
            this.transformOrigin = TransformOrigin.Center
            this.rotationX = rotationXInRadians
            this.rotationY = rotationYInRadians
            this.rotationZ = rotationZInRadians
            this.cameraDistance = 8 * density // Adjust camera distance for more depth
        }
    ) {
        content()
    }
}

@Composable
fun CocktailCardRenderer(
    offset: Float,
    cardWidth: Dp = 250.dp,
    cardHeight: Dp,
    cocktail: Drinks,
    onClick: (Drinks) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) CardDarkColor else CardLightColor

    Box(
        modifier = Modifier
            .width(cardWidth)
            .padding(top = 8.dp)
    ) {
        // Background and shadow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .shadow(
                    elevation = 10.dp + 6.dp * abs(offset),
                    shape = RoundedCornerShape(8.dp),
                    clip = false
                )
        )

        // Column for image and text
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Ensures image is at the top
        ) {
            // Cocktail image with parallax effect
            Box(
                modifier = Modifier
                    .offset(y = (-15).dp)
                    .clickable { onClick(cocktail) }
            ) {
                CocktailImageLayer(
                    offset = offset,
                    cardWidth = cardWidth,
                    cardHeight = cardHeight,
                    strDrinkThumb = cocktail.strDrinkThumb
                )
            }

            // Spacer to ensure the text is pushed to the bottom
            Spacer(modifier = Modifier.weight(1f))

            // Cocktail data (text)
            Text(
                text = cocktail.strDrink,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.W600
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 30.dp)
            )
        }
    }
}

@Composable
fun CocktailImageLayer(
    offset: Float,
    cardWidth: Dp,
    cardHeight: Dp,
    strDrinkThumb: String
) {
    val maxParallax = 30.dp
    val globalOffset = with(LocalDensity.current) { offset * maxParallax.toPx() * 2 }
    val cardPadding = 28.dp
    val containerWidth = cardWidth - cardPadding

    Box(
        modifier = Modifier
            .height(cardHeight * 0.6f)
            .width(containerWidth)
    ) {
        PositionedLayer(
            path = strDrinkThumb,
            width = containerWidth.value * 0.8f,
            maxOffset = maxParallax * 0.1f,
            globalOffset = globalOffset
        )
        PositionedLayer(
            path = strDrinkThumb,
            width = containerWidth.value * 0.9f,
            maxOffset = maxParallax * 0.6f,
            globalOffset = globalOffset
        )
        PositionedLayer(
            path = strDrinkThumb,
            width = containerWidth.value * 0.9f,
            maxOffset = maxParallax,
            globalOffset = globalOffset
        )
    }
}

@Composable
fun PositionedLayer(
    path: String,
    width: Float,
    maxOffset: Dp,
    globalOffset: Float
) {
    val context = LocalContext.current

    val offsetX = ((width / 2) - width / 2 - globalOffset * maxOffset.value).dp

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = (width * 0.45f).dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(path)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.width(width.dp)
        )
    }
}




@Composable
fun LoadingScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.intro_loading))
    LottieAnimation(
        composition = composition,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("An error occurred")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}




