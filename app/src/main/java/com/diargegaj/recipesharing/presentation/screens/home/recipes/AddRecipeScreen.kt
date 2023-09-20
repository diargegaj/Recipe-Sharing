package com.diargegaj.recipesharing.presentation.screens.home.recipes

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diargegaj.recipesharing.R
import com.diargegaj.recipesharing.presentation.utils.ImagePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen() {
    var recipeImage by remember { mutableStateOf<ImageBitmap?>(null) }

    var ingredients by remember {
        mutableStateOf(listOf(""))
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
                    recipeImage = pickedImage
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TextField(
                value = "",
                onValueChange = { },
                label = { Text(text = stringResource(id = R.string.recipe_title)) }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TextField(
                value = "",
                onValueChange = { },
                label = { Text(text = stringResource(id = R.string.recipe_description)) })

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text(text = stringResource(id = R.string.ingredients), fontWeight = FontWeight.Bold)
        }

        items(ingredients.size) { index ->
            IngredientTextField(currentValue = ingredients[index]) { newValue ->

                ingredients = ingredients.toMutableList().also {
                    it[index] = newValue
                }

            }
        }

        item {
            IconButton(onClick = {
                if (ingredients.last().isNotEmpty()) {
                    ingredients = ingredients + ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add ingredient")
            }
        }

        item {
            Button(onClick = {
                TODO(" Handle storing recipe.")
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