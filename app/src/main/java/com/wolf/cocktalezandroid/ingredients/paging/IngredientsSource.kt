package com.wolf.cocktalezandroid.ingredients.paging


import Ingredient
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi

import retrofit2.HttpException
import java.io.IOException

class IngredientsSource(
    private val api: CocktailDbApi
) : PagingSource<Int, Ingredient>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Ingredient> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.getIngredients()
            LoadResult.Page(
                data = response.ingredients,
                prevKey = null, // Only paging forward.
                nextKey = if (response.ingredients.isEmpty()) null else nextPageNumber + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Ingredient>): Int? {
        return state.anchorPosition
    }
}