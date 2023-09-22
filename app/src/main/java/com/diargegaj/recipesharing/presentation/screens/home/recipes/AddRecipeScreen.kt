package com.diargegaj.recipesharing.presentation.screens.home.recipes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.utils.ImagePicker
import com.diargegaj.recipesharing.presentation.viewModel.home.recipes.AddRecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    viewModel: AddRecipeViewModel = hiltViewModel()
) {
    val state by viewModel.recipeState.collectAsState()
    val recipeImage by viewModel.recipeImage.collectAsState()

    val errorMessages by viewModel.errorMessages.collectAsState(initial = "")

    if (errorMessages.isNotEmpty()) {
        Toast.makeText(LocalContext.current, errorMessages, Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ImagePicker(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(Color.Gray),
                image = recipeImage,
                editView = {
                    Text(text = stringResource(id = R.string.tap_to_upload_an_image))
                },
                onImagePicked = { pickedImage ->
                    viewModel.updatePickedImage(
                        pickedImage = pickedImage
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TextField(
                value = state.title,
                onValueChange = { newValue ->
                    viewModel.updateTitle(newValue)
                },
                label = { Text(text = stringResource(id = R.string.recipe_title)) }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TextField(
                value = state.description,
                onValueChange = { newValue ->
                    viewModel.updateDescription(newValue)
                },
                label = { Text(text = stringResource(id = R.string.recipe_description)) })

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(text = stringResource(id = R.string.ingredients), fontWeight = FontWeight.Bold)
        }

        items(state.ingredients.size) { index ->
            IngredientTextField(currentValue = state.ingredients[index]) { newValue ->
                viewModel.updateIngredient(
                    index = index,
                    newValue = newValue
                )
            }
        }

        item {
            IconButton(onClick = {
                viewModel.addIngredient()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add ingredient")
            }
        }

        item {
            Button(onClick = {
                viewModel.storeRecipe()
            }) {
                Text(text = stringResource(id = R.string.add_recipe))
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientTextField(currentValue: String, onValueChange: (String) -> Unit) {
    TextField(
        value = currentValue,
        onValueChange = onValueChange,
        label = { Text("Ingredient") }
    )
    Spacer(modifier = Modifier.padding(2.dp))

}