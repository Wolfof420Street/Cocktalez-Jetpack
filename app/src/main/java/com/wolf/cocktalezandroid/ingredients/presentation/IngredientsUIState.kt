package com.wolf.cocktalezandroid.ingredients.presentation


import Ingredient
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class IngredientsUIState (

    // Ingredients
    val ingredients: Flow<PagingData<Ingredient>> = emptyFlow(),


    val pageIndex: Int = 0,


)