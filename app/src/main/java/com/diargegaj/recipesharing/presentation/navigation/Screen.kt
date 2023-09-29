package com.diargegaj.recipesharing.presentation.navigation

sealed class Screen(val route: String) {

    object SplashScreen : Screen("splash_screen")

    object LoginScreen : Screen("login")
    object RegisterScreen : Screen("register")

    object HomeScreen : Screen("home")
    object RecipesScreen : Screen("recipes")
    object RecipeDetails : Screen("recipe_details")

    object Settings : Screen("settings")

    object AccountInfo : Screen("account_info")
    object ChangeEmail: Screen("change_email")
    object ChangePassword: Screen("change_password")


    object ProfileInfo : Screen("profile_info")

}
