package com.wolf.cocktalezandroid.glass.presentation

sealed interface GlassScreenUiEvents {
    data object NavigateToGlassDetail : GlassScreenUiEvents
    data object RefreshGlasses : GlassScreenUiEvents
    data object OnGlassSelected : GlassScreenUiEvents
    data object ShowErrorDialog : GlassScreenUiEvents
}

