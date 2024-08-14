package com.wolf.cocktalezandroid.ingredientcocktails.domain.repository

import Drinks
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface IngredientCocktailRepository {

    fun getGlassesByIngredients(glass: String) : Flow<PagingData<Drinks>>

}