package com.diargegaj.recipesharing.presentation.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

class RecipeNavigationActions(val navController: NavHostController) {

    fun navigateToLogin() {
        navController.navigate(Screen.LoginScreen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToRegister() {
        navController.navigate(Screen.RegisterScreen.route)
    }

    fun navigateToHome() {
        navController.navigate(Screen.HomeScreen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToRecipeDetails(recipeId: String) {
        navController.navigate(Screen.RecipeDetails.route + "/$recipeId")
    }

}