package com.wolf.cocktalezandroid.home.data.paging

import CocktailObject
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import retrofit2.HttpException
import java.io.IOException

class CocktailDetailsSource(
    private val api: CocktailDbApi,
    private val cocktailId: String
) : PagingSource<Int, CocktailObject>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CocktailObject> {
        return try {
            val response = api.getCocktailDetails(cocktailId)
            LoadResult.Page(
                data = listOf(response.drinks.firstOrNull() ?: return LoadResult.Error(Exception("Cocktail not found"))),
                prevKey = null, // Only paging forward.
                nextKey = null // No paging for details
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CocktailObject>): Int? {
        return state.anchorPosition
    }
}
