package com.wolf.cocktalezandroid.ingredientcocktails.presentation

import Drinks
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class IngredientCocktailUiState (

    val cocktails : Flow<PagingData<Drinks>> = emptyFlow(),

    )