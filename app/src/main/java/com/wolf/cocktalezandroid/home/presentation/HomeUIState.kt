package com.wolf.cocktalezandroid.home.presentation


import CocktailObject
import Drinks
import Glass
import androidx.paging.PagingData

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class CocktailUiState(
    // Cocktails
    val alcoholicCocktails: Flow<PagingData<Drinks>> = emptyFlow(),
    val nonAlcoholicCocktails: Flow<PagingData<Drinks>> = emptyFlow(),
    val popularCocktails: Flow<PagingData<CocktailObject>> = emptyFlow(),

    val selectedTabIndex: Int = 0,

    val searchQuery : String = "",

    // Glasses
    val glasses: Flow<PagingData<Glass>> = emptyFlow(),


    // Additional properties for UI
    val offset: Float = 0f,
    val cardWidth: Float = 250f,
    val cardHeight: Float = 200f
)