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

    fun navigateToRecipes(userId: String = "") {
        navController.navigate(Screen.RecipesScreen.route + "/$userId")
    }

    fun navigateToSettings() {
        navController.navigate(Screen.Settings.route)
    }

    fun navigateToEditRecipe(recipeId: String) {
        navController.navigate(Screen.EditRecipe.route + "/$recipeId")
    }

    fun navigateTo(route: String) {
        navController.navigate(route)
    }

    fun goBack() {
        navController.popBackStack()
    }

}