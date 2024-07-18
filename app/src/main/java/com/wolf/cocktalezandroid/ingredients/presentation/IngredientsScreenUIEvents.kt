package com.wolf.cocktalezandroid.ingredients.presentation

sealed interface IngredientsScreenUiEvents {
    data object NavigateToIngredientDetail : IngredientsScreenUiEvents
    data object RefreshIngredients : IngredientsScreenUiEvents
    data object OnIngredientSelected : IngredientsScreenUiEvents
    data object ShowErrorDialog : IngredientsScreenUiEvents
}