package com.wolf.cocktalezandroid.cocktaildetail.presentation

import CocktailObject

data class CocktailDetailsUiState(
    val isLoading: Boolean = false,
    val cocktailDetails: CocktailObject? = null,
    val error: String? = null
)
