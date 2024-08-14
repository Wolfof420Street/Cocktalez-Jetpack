package com.wolf.cocktalezandroid.glassescocktails.presentation

import Drinks

sealed interface GlassCocktailUIEvents {

    data object PullToRefresh : GlassCocktailUIEvents

    data class OnDrinkSelected(
        val glass : Drinks
    ) : GlassCocktailUIEvents

    data object NavigateBack : GlassCocktailUIEvents

}