package com.wolf.cocktalezandroid.home.data.repository

import CocktailObject
import Drinks
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import com.wolf.cocktalezandroid.common.util.Constants.PAGING_SIZE
import com.wolf.cocktalezandroid.home.data.paging.AlcoholicCocktailsSource
import com.wolf.cocktalezandroid.home.data.paging.CategoryCocktailsSource
import com.wolf.cocktalezandroid.home.data.paging.GlassCocktailsSource
import com.wolf.cocktalezandroid.home.data.paging.IngredientCocktailsSource
import com.wolf.cocktalezandroid.home.data.paging.NonAlcoholicCocktailsSource
import com.wolf.cocktalezandroid.home.data.paging.PopularCocktailsSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class CocktailsRepository @Inject constructor(private val api: CocktailDbApi) {

    fun getAlcoholicCocktails(): Flow<PagingData<Drinks>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE),
            pagingSourceFactory = {
                AlcoholicCocktailsSource(api)
            }
        ).flow
    }


    fun getNonAlcoholicCocktails(): Flow<PagingData<Drinks>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE),
            pagingSourceFactory = {
                NonAlcoholicCocktailsSource(api)
            }
        ).flow
    }

    fun getPopularCocktails(): Flow<PagingData<CocktailObject>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE),
            pagingSourceFactory = {
                PopularCocktailsSource(api)
            }
        ).flow
    }

    fun getCocktailsByGlass(glass: String): Flow<PagingData<Drinks>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE),
            pagingSourceFactory = {
                GlassCocktailsSource(api, glass)
            }
        ).flow
    }

    fun getCocktailsByCategory(category: String): Flow<PagingData<Drinks>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE),
            pagingSourceFactory = {
                CategoryCocktailsSource(api, category)
            }
        ).flow
    }

    fun getCocktailsByIngredient(ingredient: String): Flow<PagingData<Drinks>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE),
            pagingSourceFactory = {
                IngredientCocktailsSource(api, ingredient)
            }
        ).flow
    }
}