package com.wolf.cocktalezandroid.glass.presentation

import Glass

sealed interface GlassScreenUiEvents {

    data object PullToRefresh : GlassScreenUiEvents
    data class OnGlassSelected(
        val glass : Glass
    ) : GlassScreenUiEvents

    data class NavigateToCocktailsScreen(
        val glassName : String
    ) : GlassScreenUiEvents

    data object NavigateBack : GlassScreenUiEvents

}

