package com.diargegaj.recipesharing.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.diargegaj.recipesharing.presentation.navigation.Navigation
import com.diargegaj.recipesharing.ui.theme.RecipeSharingTheme

@Composable
fun RecipeApplication() {

    RecipeSharingTheme {
        val navController = rememberNavController()
        Navigation(navController = navController)
    }

}