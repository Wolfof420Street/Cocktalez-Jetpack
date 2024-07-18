

package com.wolf.cocktalezandroid.ingredients.domain.repository

import Drinks
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface IngredientsRepository {
    fun getIngredients(): Flow<PagingData<Drinks>>
}
