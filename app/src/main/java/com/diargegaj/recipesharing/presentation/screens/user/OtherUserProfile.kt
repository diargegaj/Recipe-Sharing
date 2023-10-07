package com.diargegaj.recipesharing.presentation.screens.user

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.screens.home.profile.ProfileScreen

@Composable
fun OtherUserProfile(
    backStackEntry: NavBackStackEntry,
    recipeNavigationActions: RecipeNavigationActions
) {

    ProfileScreen(
        recipeNavigationActions = recipeNavigationActions,
        backStackEntry = backStackEntry
    )
}