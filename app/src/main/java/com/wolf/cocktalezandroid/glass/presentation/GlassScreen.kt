package com.wolf.cocktalezandroid.glass.presentation

import android.content.Context
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalSharedTransitionApi::class)
@Destination<RootGraph>
@Composable
fun SharedTransitionScope.GlassScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigator: DestinationsNavigator,
    viewModel: GlassViewModel = hiltViewModel()
) {
    val glassUiState by viewModel.glassUiState.collectAsState()

    val context = LocalContext.current



    GlassScreenContent(
        state = glassUiState,
        animatedVisibilityScope = animatedVisibilityScope,
        context = context,
        viewModel = viewModel,
        onEvent = { event ->

            when (event) {

                is GlassScreenUiEvents.OnGlassSelected -> {

                }

                GlassScreenUiEvents.PullToRefresh -> {
                    viewModel.refresh()
                }

                GlassScreenUiEvents.NavigateBack -> {
                    navigator.navigateUp()
                }
            }})
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class
)
fun SharedTransitionScope.GlassScreenContent(
    state:  GlassScreenUiState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onEvent: (GlassScreenUiEvents) -> Unit,
    context: Context,
    viewModel: GlassViewModel
) {

}