package com.wolf.cocktalezandroid.home.presentation

import AppHeader
import CocktailObject
import Drinks
import android.content.Context
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.PI
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
        placeholder = { Text("Search for Cocktails") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp ,
                    vertical = 16.dp
                ),
        shape = RoundedCornerShape(30.dp),
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
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
                    normalizedOffset = normalizedOffset,
                    items = popularCocktails,
                    onClick = onClick,
                    animatedVisibilityScope = animatedVisibilityScope
                )
                1 -> AlcoholicCocktails(
                    items = alcoholicCocktails,
                    onClick = onClick,
                    cardHeight = cardHeight,
                    cardWidth = cardWidth,
                    normalizedOffset = normalizedOffset,
                    animatedVisibilityScope = animatedVisibilityScope
                )
                2 -> NonAlcoholicCocktails(
                    cardHeight = cardHeight,
                    cardWidth = cardWidth,
                    normalizedOffset = normalizedOffset,
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
    normalizedOffset : Float,
    cardHeight : Dp,
    cardWidth : Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {


    CardRow(items = items,
        modifier = Modifier.fillMaxWidth(),
        content = {
            Rotation3d(
                rotationX = 0f,
                rotationY = normalizedOffset * 20,
                rotationZ = 0f) {
                CocktailCardRenderer(
                    offset = normalizedOffset,
                    cocktail = it,
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
    normalizedOffset : Float,
    cardHeight : Dp,
    cardWidth : Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    CardRow(items = items,
        modifier = Modifier.fillMaxWidth(),
        content = {
            Rotation3d(
                rotationX = 0f,
                rotationY = normalizedOffset * 20,
                rotationZ = 0f) {
                CocktailCardRenderer(
                    offset = normalizedOffset,
                    cocktail = it,
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
    normalizedOffset : Float,
    cardHeight : Dp,
    cardWidth : Dp,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    CardRow(items = items,
        modifier = Modifier.fillMaxWidth(),
        content = {
            Rotation3d(
                rotationX = 0f,
                rotationY = normalizedOffset * 20,
                rotationZ = 0f) {
                CocktailCardRenderer(
                    offset = normalizedOffset,
                     cocktail = Drinks(
                        strDrink = it.strDrink,
                        strDrinkThumb = it.strDrinkThumb,
                        idDrink = it.idDrink
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
fun <T : Any> CardRow (
    modifier: Modifier = Modifier,
    items: LazyPagingItems<T>,
    content: @Composable (T) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.itemCount) {
                val item = items[it]
                if (item != null) {
                    content(item)
                }
            }
            items.loadState.let { loadState ->
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .align(Alignment.Center),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                               LoadingScreen()
                            }
                        }
                    }

                    loadState.refresh is LoadState.NotLoading && items.itemCount < 1 -> {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .align(Alignment.Center),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = "No data available",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }


                    loadState.refresh is LoadState.Error -> {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .align(Alignment.Center),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
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
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                               LoadingScreen()
                            }
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "An error occurred",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
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
fun Rotation3d(
    rotationX: Float,
    rotationY: Float,
    rotationZ: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val degreesToRadians = PI / 180f
    val rotationXed = rotationX * degreesToRadians.toFloat()
    val rotationYed = rotationY * degreesToRadians.toFloat()
    val rotationZed = rotationZ * degreesToRadians.toFloat()

    Box(
        modifier = modifier.graphicsLayer {
            this.rotationX = rotationXed
            this.rotationY = rotationYed
            this.rotationZ = rotationZed
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
    // Animate the rotation based on the offset value
    val rotationY by animateFloatAsState(
        targetValue = offset * 30f,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing),
        label = ""
    )

    Box(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .padding(12.dp)
            .graphicsLayer(rotationY = rotationY) // Apply the rotation animation
            .clickable { onClick(cocktail) },
        contentAlignment = Alignment.Center
    ) {
        // Card background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .shadow(
                    elevation = 4.dp * offset.absoluteValue,
                    shape = RoundedCornerShape(8.dp)
                )
        )

        // Cocktail image
        CocktailImageLayer(
            offset = offset,
            cardWidth = cardWidth,
            cardHeight = cardHeight,
            strDrinkThumb = cocktail.strDrinkThumb
        )

        // Cocktail data
        CocktailData(
            cardHeight = cardHeight,
            strDrink = cocktail.strDrink
        )
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
    val globalOffset = offset * maxParallax.value * 2
    val cardPadding = 28.dp
    val containerWidth = cardWidth - cardPadding

    Box(
        modifier = Modifier
            .height(cardHeight + 60.dp)
            .width(containerWidth)
    ) {
        PositionedLayer(strDrinkThumb, containerWidth.value * 0.8f, maxParallax * 0.1f, globalOffset)
        PositionedLayer(strDrinkThumb, containerWidth.value * 0.9f, maxParallax * 0.6f, globalOffset)
        PositionedLayer(strDrinkThumb, containerWidth.value * 0.9f, maxParallax, globalOffset)
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
    Box(
        modifier = Modifier
            .offset(
                x = ((width / 2) - width / 2 - globalOffset * maxOffset.value).dp,
                y = (width * 0.45f).dp
            )
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
fun CocktailData(cardHeight: Dp, strDrink: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(cardHeight * 0.4f))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = strDrink,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 20.dp)
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




