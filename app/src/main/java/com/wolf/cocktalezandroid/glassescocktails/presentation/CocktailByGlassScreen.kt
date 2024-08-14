package com.wolf.cocktalezandroid.glassescocktails.presentation

import AppHeader
import Drinks
import android.content.Context
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CocktailDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wolf.cocktalezandroid.home.presentation.LoadingScreen
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalSharedTransitionApi::class)
@Destination<RootGraph>
@Composable
fun SharedTransitionScope.CocktailByGlassScreen(
    cocktailName : String,
    navigator: DestinationsNavigator,
    viewModel: GlassCocktailViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.getCocktailsByGlass(cocktailName)
    }

    val cocktailGlassState by viewModel.glassCocktailUIState.collectAsState()

    val context = LocalContext.current


    CocktailByGlassScreenContent(
        state = cocktailGlassState,
        context = context,
        cocktailName = cocktailName,
        onEvent = {
            event ->

            when (event) {
                is GlassCocktailUIEvents.NavigateBack -> {
                    navigator.navigateUp()
                }

                is GlassCocktailUIEvents.OnDrinkSelected -> {
                    navigator.navigate(
                        CocktailDetailsScreenDestination(
                            cocktailId = event.glass.idDrink
                        ))
                }
                GlassCocktailUIEvents.PullToRefresh -> {
                    viewModel.getCocktailsByGlass(cocktailName)
                }
            }
        }
    )

}


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.CocktailByGlassScreenContent(
    state: GlassCocktailUiState,
    onEvent: (GlassCocktailUIEvents) -> Unit,
    context: Context,
    cocktailName: String
) {

    val drinks = state.cocktails.collectAsLazyPagingItems()

    Scaffold (
        topBar = {
            AppHeader(
                onBackArrowClicked = {
                    onEvent(GlassCocktailUIEvents.NavigateBack)
                },
                modifier = Modifier.fillMaxWidth(),
                showBackArrow = true,
                title = {
                    Text(
                        cocktailName
                    )
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            isRefreshing = false,
            onRefresh = {
                onEvent(GlassCocktailUIEvents.PullToRefresh)
            }) {
            when {
                drinks.loadState.refresh is LoadState.Loading -> {
                    LoadingScreen()
                }

                drinks.loadState.refresh is LoadState.NotLoading && drinks.itemCount < 1 -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = "No data available",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }

                drinks.loadState.refresh is LoadState.Error -> {
                    val errorMessage = when ((drinks.loadState.refresh as LoadState.Error).error) {
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

                else ->  {
                    CocktailGrid(
                        context = context,
                        drinks = drinks,
                        onDrinkClick = { drink ->
                            onEvent(GlassCocktailUIEvents.OnDrinkSelected(drink))
                        }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CocktailGrid(
    drinks: LazyPagingItems<Drinks>,
    onDrinkClick: (Drinks) -> Unit,
    context: Context
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.padding(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(drinks.itemCount) { index ->
            drinks[index]?.let { drinkItem ->
                GlassTile(
                    context = context,
                    data = drinkItem,
                    onPressed = {
                        onDrinkClick(drinkItem)
                    }
                )
            }
        }
    }
}

@Composable
fun GlassTile(
    data: Drinks,
    onPressed: (Drinks) -> Unit,
    context : Context
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onPressed(data) },
        elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(data.strDrinkThumb)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(3f / 4f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.strDrink,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}


