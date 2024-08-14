package com.wolf.cocktalezandroid.glassescocktails.presentation

import Drinks
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class GlassCocktailUiState (

    val cocktails : Flow<PagingData<Drinks>> = emptyFlow(),

)