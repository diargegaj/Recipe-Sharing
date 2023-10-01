package com.diargegaj.recipesharing.presentation.screens.home.recipes.editRecipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.navigation.RecipeNavigationActions
import com.diargegaj.recipesharing.presentation.screens.home.recipes.IngredientTextField
import com.diargegaj.recipesharing.presentation.screens.home.recipes.recipeDetails.RecipeImage
import com.diargegaj.recipesharing.presentation.utils.DefaultAppBar
import com.diargegaj.recipesharing.presentation.viewModel.home.recipes.editRecipe.EditRecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    recipeNavigationActions: RecipeNavigationActions,
    backStackEntry: NavBackStackEntry,
    viewModel: EditRecipeViewModel = hiltViewModel(backStackEntry)
) {
    val recipe by viewModel.recipeState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(id = R.string.edit_recipe),
                    navigationIcon = Icons.Default.ArrowBack,
                    onNavigationClick = {
                        recipeNavigationActions.goBack()
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    RecipeImage(
                        imageUrl = recipe.imageUrl,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.recipe_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        TextField(
                            value = recipe.title,
                            onValueChange = { viewModel.updateTitle(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.recipe_description),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        TextField(
                            value = recipe.description,
                            onValueChange = { viewModel.updateDescription(it) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Text(
                        text = stringResource(id = R.string.ingredients),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(recipe.ingredients.size) { index ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IngredientTextField(
                            currentValue = recipe.ingredients[index],
                            onValueChange = { newValue ->
                                viewModel.updateIngredient(index, newValue)
                            })

                        IconButton(onClick = {
                            viewModel.deleteIngredient(index)
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    IconButton(onClick = {
                        viewModel.addIngredient()
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add ingredient")
                    }
                }

                item {
                    Button(onClick = { viewModel.saveChanges() }) {
                        Text(text = stringResource(id = R.string.save_changes))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}