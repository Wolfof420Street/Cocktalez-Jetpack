package com.wolf.cocktalezandroid.glass.presentation

import AppHeader
import android.content.Context
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CocktailByIngredientScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wolf.cocktalezAndroid.R
import com.wolf.cocktalezandroid.home.presentation.LoadingScreen
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.sin

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
        context = context,
        onEvent = { event ->

            when (event) {


                is GlassScreenUiEvents.OnGlassSelected -> {
                    viewModel.selectGlass(event.glass)
                }

                GlassScreenUiEvents.PullToRefresh -> {
                    viewModel.refresh()
                }

                GlassScreenUiEvents.NavigateBack -> {
                    navigator.navigateUp()
                }

                is GlassScreenUiEvents.NavigateToCocktailsScreen -> {
                    navigator.navigate(
                        CocktailByIngredientScreenDestination(
                            event.glassName
                        ))
                 }

            }})
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class
)
fun SharedTransitionScope.GlassScreenContent(
    state:  GlassScreenUiState,
    onEvent: (GlassScreenUiEvents) -> Unit,
    context: Context,
) {

    val glasses = state.glasses.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
           AppHeader(
              title = {
                  Text(
                      context.getString(R.string.glass_header)
                  )
              }
           )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            isRefreshing = false,
            onRefresh = {
                onEvent(GlassScreenUiEvents.PullToRefresh)
            }
        ) {

            when {
                glasses.loadState.refresh is LoadState.Loading -> {
                    LoadingScreen()
                }

                glasses.loadState.refresh is LoadState.NotLoading && glasses.itemCount < 1 -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = "No data available",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                }

                glasses.loadState.refresh is LoadState.Error -> {
                    val errorMessage = when ((glasses.loadState.refresh as LoadState.Error).error) {
                        is HttpException -> "Oops, something went wrong!"
                        is IOException -> "Couldn't reach server, check your internet connection!"
                        else -> "Unknown error occurred"
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                else -> {
                    LazyColumn {
                        items(glasses.itemCount) { index ->
                            glasses[index]?.let {
                                GlassCard(
                                    glassName = it.strGlass,
                                    onViewClicked = {
                                        onEvent(GlassScreenUiEvents.NavigateToCocktailsScreen(it.strGlass))
                                    },
                                    onClick = { onEvent(GlassScreenUiEvents.OnGlassSelected(it)) },
                                    isOpen = it == state.selectedGlass
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun GlassCard(
    glassName: String,
    isOpen: Boolean,
    onClick: () -> Unit,
    onViewClicked: () -> Unit
) {
    val cardHeight by animateDpAsState(
        label = glassName,
        targetValue = if (isOpen) 220.dp else 96.dp,
        animationSpec = tween(durationMillis = 1200,
            easing = FastOutSlowInEasing)
    )

    val liquidSim1 = remember { LiquidSimulation() }
    val liquidSim2 = remember { LiquidSimulation() }
    val animationController = remember { Animatable(0f) }

    LaunchedEffect(isOpen) {
        if (isOpen) {
            liquidSim1.start(animationController)
            liquidSim2.start(animationController)
            animationController.animateTo(1f, animationSpec = tween(3000))
        } else {
            animationController.animateTo(0f, animationSpec = tween(3000))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .height(cardHeight)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
    ) {
        if (isOpen) {
            LiquidBackground(
                simulation1 = liquidSim1,
                simulation2 = liquidSim2,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = glassName.uppercase(),
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                )
            )
            if (isOpen) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Have a look at our $glassName cocktails!",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    onViewClicked()
                }) {
                    Text("View All Cocktails")
                }
            }
        }
    }
}


class LiquidSimulation {
    var ctrlPts = mutableListOf<Offset>()
    var endPts = mutableListOf<Offset>()
    private val ease = FastOutSlowInEasing

    init {
        val random = kotlin.random.Random
        val curveCount = 4
        val gap = 1f / (curveCount * 2f)
        endPts.add(Offset(0f, 0f))
        for (i in 1 until curveCount) {
            endPts.add(Offset(gap * i * 2f, 0f))
        }
        endPts.add(Offset(1f, 0f))

        for (i in 0..curveCount) {
            val height = (0.5f + random.nextFloat() * 0.5f) * if (i % 2 == 0) 1f else -1f
            ctrlPts.add(Offset(gap + gap * i * 2f, height))
        }
    }

    fun start(animationController: Animatable<Float, AnimationVector1D>) {
        while (animationController.isRunning) {
            for (i in ctrlPts.indices) {
                val offset = ctrlPts[i]
                val newY = offset.y + 0.01f * sin(animationController.value.toDouble()).toFloat()
                ctrlPts[i] = Offset(offset.x, newY)
            }
        }
    }
}

@Composable
fun LiquidBackground(
    simulation1: LiquidSimulation,
    simulation2: LiquidSimulation,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val waveHeight = 100f
        val color1 = Color(0xffC48D3B).copy(alpha = 0.4f)
        val color2 = Color(0xff9D7B32).copy(alpha = 0.4f)

        drawIntoCanvas { canvas ->
            drawWave(simulation1, canvas, size.width, size.height, waveHeight, color1)
            drawWave(simulation2, canvas, size.width, size.height, waveHeight, color2)
        }
    }
}

fun drawWave(
    simulation: LiquidSimulation,
    canvas: androidx.compose.ui.graphics.Canvas,
    width: Float,
    height: Float,
    waveHeight: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(0f, height)
        for (i in 0 until minOf(simulation.ctrlPts.size, simulation.endPts.size - 1)) {
            val ctrl = simulation.ctrlPts[i]
            val end = simulation.endPts[i + 1]
            quadraticTo(
                ctrl.x * width, ctrl.y * waveHeight,
                end.x * width, height
            )
        }
        lineTo(width, height)
        lineTo(0f, height)
    }

    canvas.drawPath(path, androidx.compose.ui.graphics.Paint().apply { this.color = color })
}
