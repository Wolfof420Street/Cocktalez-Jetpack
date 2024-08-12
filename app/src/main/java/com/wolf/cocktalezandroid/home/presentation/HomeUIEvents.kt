package com.wolf.cocktalezandroid.home.presentation

import CocktailObject
import Drinks

sealed interface CocktailUiEvents {

    data object NavigateBack : CocktailUiEvents
    data object OnPullToRefresh : CocktailUiEvents

    data class OnTabSelected(val tabIndex: Int) : CocktailUiEvents

    data class OnSearchQueried(val query: String) : CocktailUiEvents

    data class NavigateToCocktailDetails(
        val cocktail: Drinks,
    ) : CocktailUiEvents


}