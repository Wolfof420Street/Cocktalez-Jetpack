package com.wolf.cocktalezandroid.ingredients.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wolf.cocktalezandroid.glass.presentation.GlassViewModel

@Destination<RootGraph>
@Composable
fun IngredientsScreen(
    navigator: DestinationsNavigator,
    viewModel: GlassViewModel = hiltViewModel(),
) {

}