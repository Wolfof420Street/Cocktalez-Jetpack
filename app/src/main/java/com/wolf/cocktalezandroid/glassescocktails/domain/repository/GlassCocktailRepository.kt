package com.wolf.cocktalezandroid.glassescocktails.domain.repository

import Drinks
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GlassCocktailRepository {

    fun getGlassesByCategory(glass: String) : Flow<PagingData<Drinks>>

}