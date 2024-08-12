package com.wolf.cocktalezandroid.glass.paging

import Glass
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import retrofit2.HttpException
import java.io.IOException

class GlassSource(
    private val api: CocktailDbApi
) : PagingSource<Int, Glass>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Glass> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.getGlasses()
            LoadResult.Page(
                data = response.categories,
                prevKey = null, // Only paging forward.
                nextKey = if (response.categories.isEmpty()) null else nextPageNumber + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Glass>): Int? {
        return state.anchorPosition
    }
}