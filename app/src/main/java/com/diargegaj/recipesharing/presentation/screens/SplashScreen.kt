package com.diargegaj.recipesharing.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.viewModel.AuthViewModel
import com.diargegaj.recipesharing.presentation.viewModel.NavigationTarget

@Composable
fun SplashScreen(
    recipeNavigationActions: RecipeNavigationActions,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navigationEvent by authViewModel.navigationEvent.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        authViewModel.checkUserLoggedIn()
    }

    when (navigationEvent) {
        NavigationTarget.Home -> {
            recipeNavigationActions.navigateToHome()

        }

        NavigationTarget.Login -> {
            recipeNavigationActions.navigateToLogin()

        }

        else -> { }
    }

    // Your splash UI goes here
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Recipe Sharing", style = MaterialTheme.typography.bodyMedium)
    }
}