

package com.wolf.cocktalezandroid.ingredients.domain.repository


import Drinks
import Ingredient
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface IngredientsRepository {
    fun getIngredients(): Flow<PagingData<Ingredient>>

    fun getCocktailsByIngredients(ingredient: String) : Flow<PagingData<Drinks>>
}
