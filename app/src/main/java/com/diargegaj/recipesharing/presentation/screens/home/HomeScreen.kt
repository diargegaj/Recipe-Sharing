package com.diargegaj.recipesharing.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.screens.home.profile.ProfileScreen
import com.diargegaj.recipesharing.presentation.screens.home.recipes.AddRecipeScreen
import com.diargegaj.recipesharing.presentation.screens.home.recipes.RecipesScreen
import com.diargegaj.recipesharing.presentation.utils.RecipeBottomBar
import com.diargegaj.recipesharing.presentation.utils.SelectedScreenCategory
import com.diargegaj.recipesharing.presentation.utils.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

        var selectedScreenCategory by remember { mutableStateOf(SelectedScreenCategory.RECIPE_SCREEN) }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    titleText = stringResource(id = R.string.app_name),
                    navigationIcon = Icons.Default.Menu,
                    navigationIconCLick = {
                        TODO(" Handle icon click...")
                    }
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when(selectedScreenCategory) {
                        SelectedScreenCategory.RECIPE_SCREEN -> {
                            RecipesScreen()
                        }
                        SelectedScreenCategory.ADD_RECIPE -> {
                            AddRecipeScreen()
                        }
                        SelectedScreenCategory.PROFILE -> {
                            ProfileScreen()
                        }
                    }
                }
            },
            bottomBar = {
                RecipeBottomBar {
                    selectedScreenCategory = it
                }
            }
        )
    }
}