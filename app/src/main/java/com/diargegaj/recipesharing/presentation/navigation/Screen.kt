package com.diargegaj.recipesharing.presentation.navigation

sealed class Screen(val route: String) {

    object SplashScreen: Screen("splash_screen")

    object LoginScreen : Screen("login")
    object RegisterScreen: Screen("register")

    object HomeScreen: Screen("home")
    object RecipeDetails: Screen("recipe_details")

}
