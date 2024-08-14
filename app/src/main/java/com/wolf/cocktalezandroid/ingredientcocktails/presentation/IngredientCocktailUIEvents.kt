package com.wolf.cocktalezandroid.ingredientcocktails.presentation

import Drinks

sealed interface IngredientCocktailUIEvents {

    data object PullToRefresh : IngredientCocktailUIEvents

    data class OnDrinkSelected(
        val glass : Drinks
    ) : IngredientCocktailUIEvents

    data object NavigateBack : IngredientCocktailUIEvents

}