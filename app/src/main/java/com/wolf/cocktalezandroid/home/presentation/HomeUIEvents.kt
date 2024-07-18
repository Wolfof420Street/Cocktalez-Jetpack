package com.wolf.cocktalezandroid.home.presentation

import CocktailObject

sealed interface CocktailUiEvents {

    data object NavigateBack : CocktailUiEvents
    data object OnPullToRefresh : CocktailUiEvents

    data class NavigateToCocktailDetails(
        val cocktail: CocktailObject,
    ) : CocktailUiEvents

    
}