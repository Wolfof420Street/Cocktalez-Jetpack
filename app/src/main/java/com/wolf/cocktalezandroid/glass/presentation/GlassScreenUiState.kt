package com.wolf.cocktalezandroid.glass.presentation

import Glass
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class GlassScreenUiState (

    // Ingredients
    val glasses: Flow<PagingData<Glass>> = emptyFlow(),

    val pageIndex: Int = 0,


)