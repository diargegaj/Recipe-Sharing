package com.diargegaj.recipesharing.presentation.screens.home.recipes

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.viewModel.home.recipes.RecipeViewModel
import com.diargegaj.recipesharing.presentation.viewModel.search.SearchViewModel

@Composable
fun RecipesScreen(
    recipeNavigationActions: RecipeNavigationActions,
    userId: String = "",
    recipeViewModel: RecipeViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val recipes by recipeViewModel.state.collectAsState()
    val searchQuery by searchViewModel.searchQuery.collectAsState()

    LaunchedEffect(searchQuery) {
        recipeViewModel.onQuerySearchQueryChange(searchQuery, userId)
    }

    val errorMessages by recipeViewModel.messages.collectAsState(initial = "")

    if (errorMessages.isNotEmpty()) {
        Toast.makeText(LocalContext.current, errorMessages, Toast.LENGTH_SHORT).show()
    }

    RecipeList(
        recipes = recipes,
        recipeNavigationActions = recipeNavigationActions
    )

}