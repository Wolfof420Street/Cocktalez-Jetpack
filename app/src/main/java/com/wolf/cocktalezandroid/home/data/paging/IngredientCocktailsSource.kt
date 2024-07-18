package com.wolf.cocktalezandroid.home.data.paging


import Drinks
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import retrofit2.HttpException
import java.io.IOException

class IngredientCocktailsSource(
    private val api: CocktailDbApi,
    private val ingredient: String
) : PagingSource<Int, Drinks>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Drinks> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.getCocktailsByIngredient(ingredient)
            LoadResult.Page(
                data = response.drinks,
                prevKey = null, // Only paging forward.
                nextKey = if (response.drinks.isEmpty()) null else nextPageNumber + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Drinks>): Int? {
        return state.anchorPosition
    }
}