package com.example.moviefiltersample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviefiltersample.RouterMovieDetail.argKey
import com.example.moviefiltersample.component.SideTab
import com.example.moviefiltersample.ui.theme.MovieFilterSampleTheme
import com.example.moviefiltersample.view.FilterResultScreen
import com.example.moviefiltersample.view.MovieDetailScreen
import com.example.moviefiltersample.view.MovieFilterScreen
import com.example.moviefiltersample.view.MovieListScreen
import com.example.moviefiltersample.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            MovieFilterSampleTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Black
                ) { innerPadding ->
                    MovieFilterApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieFilterApp(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

//    val currentRouter = layer1Router.find {
//        it.route == currentDestination?.route
//    } ?: RouterMovieFilter

    val currentRouter = layer1Router.find {
        it.route == currentDestination?.route
    } ?: RouterMovieResult

    LaunchedEffect(Unit) {
        Log.d("Data Setting", "---start---")
        viewModel.getAllGenresList().collect{
            Log.d("Data Setting -> Genres", it.toString())
        }
        viewModel.getAllCountryList().collect{
            Log.d("Data Setting -> Countries", it.toString())
        }
        Log.d("Data Setting", "---end---")
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = RouterMovieFilter.route,
            modifier = modifier.fillMaxSize()
        ) {
            composable(
                route = RouterMovieFilter.route,
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(durationMillis = 500)
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 500))
                }
            ) {
                MovieFilterScreen(
                    onNavigateToSearchResult = {
                        navController.navigateSingleTopTo(RouterMovieResult.route)
                    }
                )
            }
            composable(
                route = RouterMovieList.route,
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(durationMillis = 500)
                    )
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 500))
                }
            ) {
                MovieListScreen(
                    onResultItemClick = { movieId ->
                        navController.navigateSingleTopTo("${RouterMovieDetail.route}/$movieId")
                    }
                )
            }
            composable(
                route = RouterMovieResult.route,
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(durationMillis = 500)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(durationMillis = 500)
                    )
                }
            ) {
                FilterResultScreen(
                    onResultItemClick = { movieId ->
                        navController.navigateSingleTopTo("${RouterMovieDetail.route}/$movieId")
                    }
                )
            }
            composable(
                route = RouterMovieDetail.route + "/{${argKey}}",
                arguments = listOf(navArgument(argKey) { type = NavType.IntType })
            ) { backStackEntry ->
                MovieDetailScreen(movieId = backStackEntry.arguments?.getInt(argKey) ?: 533535)
            }
        }
    }

    if (layer1Router.contains(currentRouter)){
        Box (
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            SideTab(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp),
                allRouters = layer1Router,
                currentRouter = currentRouter,
                onTabSelected = { routerScreen ->
                    navController.navigateSingleTopTo(routerScreen.route)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { innerPadding ->
        MovieFilterApp(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun <T: Any> rememberMutableStateListOf(vararg elements: T): SnapshotStateList<T> {
    return rememberSaveable(saver = snapshotStateListSaver()) {
        elements.toList().toMutableStateList()
    }
}

private fun <T : Any> snapshotStateListSaver() = listSaver<SnapshotStateList<T>, T>(
    save = { stateList -> stateList.toList() },
    restore = { it.toMutableStateList() },
)

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true

    }