package com.wolf.cocktalezandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.GlassScreenDestination
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.IngredientsScreenDestination
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.wolf.cocktalezandroid.common.presentation.components.AppScaffold
import com.wolf.cocktalezandroid.common.presentation.theme.CocktalezTheme
import com.wolf.cocktalezandroid.common.presentation.theme.Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            val themeValue by viewModel.theme.collectAsState(
                initial = Theme.FOLLOW_SYSTEM.themeValue,
                context = Dispatchers.Main.immediate
            )

            CocktalezTheme(themeValue) {
                val navController = rememberNavController()
                val navHostEngine = rememberNavHostEngine()

                val newBackStackEntry by navController.currentBackStackEntryAsState()
                val route = newBackStackEntry?.destination?.route

                AppScaffold(
                    navController = navController,
                    showBottomBar = route in listOf(
                        HomeScreenDestination.route,
                        IngredientsScreenDestination.route,
                        GlassScreenDestination.route
                    )
                ) { innerPadding ->
                    SharedTransitionLayout {
                        DestinationsNavHost(
                            modifier = Modifier
                                .padding(innerPadding),
                            navGraph = NavGraphs.root,
                            navController = navController,
                            engine = navHostEngine,
                            dependenciesContainerBuilder = {
                                dependency(this@SharedTransitionLayout)
                            }
                        )
                    }
                }
            }
        }
    }
}