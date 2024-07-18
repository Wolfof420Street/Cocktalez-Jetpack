package com.wolf.cocktalezandroid.home.presentation


import CocktailObject
import Drinks
import Glass
import Ingredient
import androidx.paging.PagingData
import com.wolf.cocktalezandroid.home.domain.Category

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CocktailUiState(
    // Cocktails
    val alcoholicCocktails: Flow<PagingData<Drinks>> = emptyFlow(),
    val nonAlcoholicCocktails: Flow<PagingData<Drinks>> = emptyFlow(),
    val popularCocktails: Flow<PagingData<CocktailObject>> = emptyFlow(),

    // Glasses
    val glasses: Flow<PagingData<Glass>> = emptyFlow(),

    // Categories
    val categories: Flow<PagingData<Category>> = emptyFlow(),

    // Ingredients
    val ingredients: Flow<PagingData<Ingredient>> = emptyFlow(),

    val selectedOption: String = "Cocktails",
    val selectedCategory: Category? = null,
    val selectedGlass: Glass? = null,
    val selectedIngredient: Ingredient? = null,
)