package com.wolf.cocktalezandroid.glassescocktails.data.domain.repository

import Drinks
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import com.wolf.cocktalezandroid.common.util.Constants
import com.wolf.cocktalezandroid.glassescocktails.domain.repository.GlassCocktailRepository
import com.wolf.cocktalezandroid.glassescocktails.paging.CocktailGlassSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GlassCocktailsRepositoryImpl @Inject constructor(private val api: CocktailDbApi) : GlassCocktailRepository {

    override fun getGlassesByCategory(glass: String): Flow<PagingData<Drinks>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false,
                pageSize = Constants.PAGING_SIZE),
            pagingSourceFactory = {
                CocktailGlassSource(api, glass)
            }
        ).flow
    }
}