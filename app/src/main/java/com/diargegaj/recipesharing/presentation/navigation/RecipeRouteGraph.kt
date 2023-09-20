package com.diargegaj.recipesharing.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.diargegaj.recipesharing.presentation.screens.auth.LoginScreen
import com.diargegaj.recipesharing.presentation.screens.auth.RegisterScreen

fun NavGraphBuilder.authRoute(recipeNavigationActions: RecipeNavigationActions) {
    composable(Screen.LoginScreen.route) {
        LoginScreen(recipeNavigationActions = recipeNavigationActions)
    }
    composable(Screen.RegisterScreen.route) {
        RegisterScreen(recipeNavigationActions = recipeNavigationActions)
    }
}
