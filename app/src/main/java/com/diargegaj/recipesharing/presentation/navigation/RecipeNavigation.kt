package com.diargegaj.recipesharing.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost


@Composable()
fun Navigation(navigationActions: RecipeNavigationActions) {
    NavHost(
        navController = navigationActions.navController,
        startDestination = Screen.LoginScreen.route
    ) {
        authRoute(recipeNavigationActions = navigationActions)
    }
}