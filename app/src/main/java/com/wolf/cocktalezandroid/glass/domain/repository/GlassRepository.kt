
package com.wolf.cocktalezandroid.glass.domain.repository

import Drinks
import Glass
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GlassRepository {
    fun getGlasses(): Flow<PagingData<Glass>>

    fun getGlassesByCategory(glass: String) : Flow<PagingData<Drinks>>
}
