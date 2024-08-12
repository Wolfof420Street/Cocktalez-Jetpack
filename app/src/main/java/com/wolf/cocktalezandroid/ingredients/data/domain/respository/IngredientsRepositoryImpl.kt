package com.wolf.cocktalezandroid.ingredients.data.domain.respository


import Drinks
import Ingredient
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import com.wolf.cocktalezandroid.common.util.Constants
import com.wolf.cocktalezandroid.home.data.paging.IngredientCocktailsSource
import com.wolf.cocktalezandroid.ingredients.domain.repository.IngredientsRepository
import com.wolf.cocktalezandroid.ingredients.paging.IngredientsSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IngredientsRepositoryImpl @Inject constructor(private val api: CocktailDbApi) : IngredientsRepository {

    override fun getIngredients(): Flow<PagingData<Ingredient>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = Constants.PAGING_SIZE),
            pagingSourceFactory = {
                IngredientsSource(api)
            }
        ).flow
    }

    override fun getCocktailsByIngredients(ingredient: String): Flow<PagingData<Drinks>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = Constants.PAGING_SIZE),
            pagingSourceFactory = {
                IngredientCocktailsSource(api, ingredient)
            }
        ).flow
    }
}