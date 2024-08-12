package com.wolf.cocktalezandroid.glass.data.domain.respository

import Drinks
import Glass
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wolf.cocktalezandroid.common.data.network.CocktailDbApi
import com.wolf.cocktalezandroid.common.util.Constants
import com.wolf.cocktalezandroid.glass.domain.repository.GlassRepository
import com.wolf.cocktalezandroid.glass.paging.GlassSource
import com.wolf.cocktalezandroid.home.data.paging.GlassCocktailsSource
import com.wolf.cocktalezandroid.home.domain.model.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GlassRepositoryImpl @Inject constructor(private val api: CocktailDbApi) : GlassRepository {

    override fun getGlasses(): Flow<PagingData<Glass>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = Constants.PAGING_SIZE),
            pagingSourceFactory = {
                GlassSource(api)
            }
        ).flow
    }

    override fun getGlassesByCategory(glass: String): Flow<PagingData<Drinks>> {
            return Pager(
                config = PagingConfig(enablePlaceholders = false,
                    pageSize = Constants.PAGING_SIZE),
                pagingSourceFactory = {
                    GlassCocktailsSource(api, glass)
                }
            ).flow
        }


}
