package com.wolf.cocktalezandroid.ingredients.presentation

import Drinks
import Ingredient

sealed interface IngredientsScreenUiEvents {
    data class OnIngredientTapped(
        val drink : Ingredient
    ) : IngredientsScreenUiEvents
    data object PullToRefresh : IngredientsScreenUiEvents

    data class PageChanged(
        val pageIndex: Int
    ) : IngredientsScreenUiEvents

    data object NavigateBack : IngredientsScreenUiEvents

}