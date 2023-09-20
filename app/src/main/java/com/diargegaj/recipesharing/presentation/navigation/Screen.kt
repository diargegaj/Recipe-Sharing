package com.diargegaj.recipesharing.presentation.navigation

sealed class Screen(val route: String) {

    object LoginScreen : Screen("login")
    object RegisterScreen: Screen("register")

}
