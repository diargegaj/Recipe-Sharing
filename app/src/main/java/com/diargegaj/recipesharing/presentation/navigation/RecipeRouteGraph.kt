package com.diargegaj.recipesharing.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.diargegaj.recipesharing.presentation.screens.auth.LoginScreen
import com.diargegaj.recipesharing.presentation.screens.auth.RegisterScreen
import com.diargegaj.recipesharing.presentation.screens.home.HomeScreen
import com.diargegaj.recipesharing.presentation.screens.home.recipes.RecipesWithHeader
import com.diargegaj.recipesharing.presentation.screens.home.recipes.recipeDetails.RecipeDetailsScreen
import com.diargegaj.recipesharing.presentation.screens.settings.SettingsScreen
import com.diargegaj.recipesharing.presentation.screens.settings.editAccount.ChangeEmailScreen
import com.diargegaj.recipesharing.presentation.screens.settings.editAccount.ChangePasswordScreen
import com.diargegaj.recipesharing.presentation.screens.settings.editAccount.EditAccountScreen
import com.diargegaj.recipesharing.presentation.screens.settings.editProfile.ChangeNameScreen
import com.diargegaj.recipesharing.presentation.screens.settings.editProfile.ChangeProfilePictureScreen
import com.diargegaj.recipesharing.presentation.screens.settings.editProfile.EditProfileScreen

fun NavGraphBuilder.authRoute(recipeNavigationActions: RecipeNavigationActions) {
    composable(Screen.LoginScreen.route) {
        LoginScreen(recipeNavigationActions = recipeNavigationActions)
    }
    composable(Screen.RegisterScreen.route) {
        RegisterScreen(recipeNavigationActions = recipeNavigationActions)
    }
}

fun NavGraphBuilder.homeRoute(recipeNavigationActions: RecipeNavigationActions) {
    composable(Screen.HomeScreen.route) {
        HomeScreen(recipeNavigationActions = recipeNavigationActions)
    }
    composable(
        Screen.RecipesScreen.route + "/{userId}",
        arguments = listOf(navArgument("userId") { type = NavType.StringType })
    ) { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId")
        RecipesWithHeader(
            recipeNavigationActions = recipeNavigationActions,
            userId = userId ?: ""
        )
    }
    composable(
        Screen.RecipeDetails.route + "/{recipeId}",
        arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
    ) { backStackEntry ->
        RecipeDetailsScreen(
            backStackEntry = backStackEntry,
            recipeNavigationActions = recipeNavigationActions
        )
    }
}

fun NavGraphBuilder.settingsRoute(recipeNavigationActions: RecipeNavigationActions) {
    composable(Screen.Settings.route) {
        SettingsScreen(recipeNavigationActions = recipeNavigationActions)
    }
    editAccountInfoRoute(recipeNavigationActions = recipeNavigationActions)
    editProfileInfoRoute(recipeNavigationActions = recipeNavigationActions)
}

fun NavGraphBuilder.editAccountInfoRoute(recipeNavigationActions: RecipeNavigationActions) {
    composable(Screen.AccountInfo.route) {
        EditAccountScreen(recipeNavigationActions = recipeNavigationActions)
    }
    composable(Screen.ChangeEmail.route) {
        ChangeEmailScreen()
    }
    composable(Screen.ChangePassword.route) {
        ChangePasswordScreen()
    }
}

fun NavGraphBuilder.editProfileInfoRoute(recipeNavigationActions: RecipeNavigationActions) {
    composable(Screen.ProfileInfo.route) {
        EditProfileScreen(recipeNavigationActions = recipeNavigationActions)
    }
    composable(Screen.ChangeProfilePicture.route) {
        ChangeProfilePictureScreen()
    }
    composable(Screen.ChangeName.route) {
        ChangeNameScreen()
    }
}