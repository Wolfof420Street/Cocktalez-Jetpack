package com.wolf.cocktalezandroid.ingredients.presentation

sealed interface IngredientsScreenUiEvents {
    data class OnIngredientTapped(
        val drink : String
    ) : IngredientsScreenUiEvents
    data object PullToRefresh : IngredientsScreenUiEvents

    data class PageChanged(
        val pageIndex: Int
    ) : IngredientsScreenUiEvents

    data object NavigateBack : IngredientsScreenUiEvents

}