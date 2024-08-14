package com.wolf.cocktalezandroid.ingredientcocktails.presentation

import AppHeader
import android.content.Context
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CocktailDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wolf.cocktalezandroid.glassescocktails.presentation.CocktailGrid
import com.wolf.cocktalezandroid.home.presentation.LoadingScreen
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalSharedTransitionApi::class)
@Destination<RootGraph>
@Composable
fun SharedTransitionScope.CocktailByIngredientScreen(
    ingredientName : String,
    navigator: DestinationsNavigator,
    viewModel: IngredientCocktailViewModel = hiltViewModel(),
) {

    LaunchedEffect(Unit) {
        viewModel.getCocktailsByIngredient(ingredientName)
    }

    val ingredientCocktailUiState by viewModel.ingredientCocktailUiState.collectAsState()

    val context = LocalContext.current

    CocktailByIngredientScreenContent(
        state = ingredientCocktailUiState,
        context = context,
        cocktailName = ingredientName,
        onEvent = {
                event ->

            when (event) {
                is IngredientCocktailUIEvents.NavigateBack -> {
                    navigator.navigateUp()
                }

                is IngredientCocktailUIEvents.OnDrinkSelected -> {
                    navigator.navigate(
                        CocktailDetailsScreenDestination(
                            cocktailId = event.glass.idDrink
                        )
                    )
                }
                IngredientCocktailUIEvents.PullToRefresh -> {
                    viewModel.getCocktailsByIngredient(ingredientName)
                }
            }
        }
    )

}


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.CocktailByIngredientScreenContent(
    state: IngredientCocktailUiState,
    onEvent: (IngredientCocktailUIEvents) -> Unit,
    context: Context,
    cocktailName: String
) {

    val drinks = state.cocktails.collectAsLazyPagingItems()

    Scaffold (
        topBar = {
            AppHeader(
                onBackArrowClicked = {
                    onEvent(IngredientCocktailUIEvents.NavigateBack)
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
                onEvent(IngredientCocktailUIEvents.PullToRefresh)
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
                            onEvent(IngredientCocktailUIEvents.OnDrinkSelected(drink))
                        }
                    )
                }
            }
        }

    }
}
