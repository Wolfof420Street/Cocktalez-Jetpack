
package com.wolf.cocktalezandroid.glass.domain.repository

import Drinks
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface GlassRepository {
    fun getGlasses(): Flow<PagingData<Drinks>>
}
