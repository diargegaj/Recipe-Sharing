package com.diargegaj.recipesharing.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.diargegaj.recipesharing.presentation.screens.SplashScreen


@Composable()
fun Navigation(navigationActions: RecipeNavigationActions) {
    NavHost(
        navController = navigationActions.navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(recipeNavigationActions = navigationActions)
        }
        authRoute(recipeNavigationActions = navigationActions)
        homeRoute(recipeNavigationActions = navigationActions)
        settingsRoute(recipeNavigationActions = navigationActions)
    }
}