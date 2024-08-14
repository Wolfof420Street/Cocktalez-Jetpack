package com.wolf.cocktalezandroid.ingredientcocktails.data.domain.repository

import Drinks
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import com.wolf.cocktalezandroid.common.util.Constants
import com.wolf.cocktalezandroid.ingredientcocktails.domain.repository.IngredientCocktailRepository
import com.wolf.cocktalezandroid.ingredientcocktails.paging.IngredientCocktailSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IngredientCocktailRepositoryImpl @Inject constructor(private val api: CocktailDbApi) :
    IngredientCocktailRepository {

    override fun getGlassesByIngredients(glass: String): Flow<PagingData<Drinks>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false,
                pageSize = Constants.PAGING_SIZE),
            pagingSourceFactory = {
                IngredientCocktailSource(api, glass)
            }
        ).flow
    }
}