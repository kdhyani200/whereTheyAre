package kd.dhyani.wheretheyare.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kd.dhyani.wheretheyare.ui.theme.screens.home.HomeScreen
import kd.dhyani.wheretheyare.ui.theme.screens.search.SearchScreen

@Composable
fun AppNavigation(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = NavigationItem.HomeScreen.route,
        enterTransition = {
            fadeIn(animationSpec = tween(700)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(700)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(700)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(700)
            )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(700)) + slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(700)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(700)) + slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(700)
            )
        }
    ) {
        composable(NavigationItem.HomeScreen.route) {
            HomeScreen(navHostController)
        }

        composable(NavigationItem.SearchScreen.route) {
            SearchScreen(navHostController)
        }
    }
}