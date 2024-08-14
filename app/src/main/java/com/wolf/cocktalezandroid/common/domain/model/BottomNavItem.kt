package com.wolf.cocktalezandroid.common.domain.model

import com.ramcosta.composedestinations.generated.destinations.GlassScreenDestination
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.IngredientsScreenDestination
import com.wolf.cocktalezAndroid.R

sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val route: String,
) {
    data object Home : BottomNavItem(
        title = "Home",
        icon = R.drawable.baseline_home_24,
        route = HomeScreenDestination.route
    )
    data object Ingredients: BottomNavItem(
        title = "Ingredients",
        icon = R.drawable.baseline_window_24,
        route = IngredientsScreenDestination.route,
    )
    data object Glasses: BottomNavItem(
        title = "Glasses",
        icon = R.drawable.wine_bar,
        route = GlassScreenDestination.route
    )

}