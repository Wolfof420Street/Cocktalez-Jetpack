package com.wolf.cocktalezandroid.glass.presentation

sealed interface GlassScreenUiEvents {

    data object PullToRefresh : GlassScreenUiEvents
    data object OnGlassSelected : GlassScreenUiEvents

    data object NavigateBack : GlassScreenUiEvents
}

