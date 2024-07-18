package com.wolf.cocktalezandroid.glass.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>
@Composable
fun GlassScreen(
    navigator: DestinationsNavigator,
    viewModel: GlassViewModel = hiltViewModel(),
) {

}