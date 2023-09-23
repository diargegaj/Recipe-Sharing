package com.diargegaj.recipesharing.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.screens.home.profile.ProfileScreen
import com.diargegaj.recipesharing.presentation.screens.home.recipes.AddRecipeScreen
import com.diargegaj.recipesharing.presentation.screens.home.recipes.RecipesScreen
import com.diargegaj.recipesharing.presentation.utils.RecipeBottomBar
import com.diargegaj.recipesharing.presentation.utils.SelectedScreenCategory
import com.diargegaj.recipesharing.presentation.utils.TopAppBar
import com.diargegaj.recipesharing.presentation.viewModel.home.HomeViewModel
import com.diargegaj.recipesharing.presentation.viewModel.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    recipeNavigationActions: RecipeNavigationActions,
    searchViewModel: SearchViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val homeState by homeViewModel.homeState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        val searchQuery by searchViewModel.searchQuery.collectAsState()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        if (homeState.isSearchBarVisible) {
                            TextField(
                                value = searchQuery,
                                onValueChange = {
                                    searchViewModel.onNewSearchQuery(it)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text = stringResource(id = R.string.search))
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = { }),
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                            )
                        } else {
                            Text(text = stringResource(id = R.string.app_name))
                        }
                    },
                    navigationIcon = Icons.Default.Menu,
                    navigationIconCLick = {
                        TODO(" Handle icon click...")
                    },
                    actions = {
                        if (homeState.shouldShowSearchIcon) {
                            IconButton(onClick = {
                                homeViewModel.onSearchIconClicked()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(id = R.string.search)
                                )
                            }
                        }
                    }
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (homeState.selectedScreenCategory) {
                        SelectedScreenCategory.RECIPE_SCREEN -> {
                            homeViewModel.updateSearchIconVisibility(value = true)
                            RecipesScreen(recipeNavigationActions = recipeNavigationActions)
                        }

                        SelectedScreenCategory.ADD_RECIPE -> {
                            homeViewModel.updateSearchIconVisibility(value = false)
                            AddRecipeScreen()
                        }

                        SelectedScreenCategory.PROFILE -> {
                            homeViewModel.updateSearchIconVisibility(value = false)
                            ProfileScreen(recipeNavigationActions = recipeNavigationActions)
                        }
                    }
                }
            },
            bottomBar = {
                RecipeBottomBar { selectedScreenCategory ->
                    homeViewModel.onSelectedScreenUpdated(selectedScreenCategory)
                }
            }
        )
    }
}