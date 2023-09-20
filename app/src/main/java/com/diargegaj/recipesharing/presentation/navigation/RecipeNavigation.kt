package com.diargegaj.recipesharing.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.diargegaj.recipesharing.presentation.screens.auth.LoginScreen
import com.diargegaj.recipesharing.presentation.screens.auth.RegisterScreen


@Composable()
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(Screen.LoginScreen.route) {
            LoginScreen()
        }
        composable(Screen.RegisterScreen.route) {
            RegisterScreen()
        }
    }
}