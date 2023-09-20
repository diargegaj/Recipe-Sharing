package com.diargegaj.recipesharing.presentation.viewModel.recipes

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.models.emptyRecipeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(): ViewModel() {

    private val _recipeState = MutableStateFlow(emptyRecipeModel())
    val recipeState: StateFlow<RecipeModel> = _recipeState.asStateFlow()

    private val _recipeImage = MutableStateFlow<ImageBitmap?>(null)
    val recipeImage = _recipeImage.asStateFlow()

    fun updateTitle(title: String) {
        val current = _recipeState.value
        _recipeState.value = current.copy(title = title)
    }

    fun updateDescription(description: String) {
        val current = _recipeState.value
        _recipeState.value = current.copy(description = description)
    }

    fun updateIngredient(index: Int, newValue: String) {
        val current = _recipeState.value

        val updatedIngredients = current.ingredients.toMutableList().also {
            it[index] = newValue
        }

        _recipeState.value = current.copy(
            ingredients = updatedIngredients
        )
    }

    fun addIngredient() {
        val current = _recipeState.value
        val lastIngredient = current.ingredients.last()

        if (lastIngredient.isNotEmpty()) {
            _recipeState.value = current.copy(
                ingredients = current.ingredients + ""
            )
        }
    }

    fun updatePickedImage(pickedImage: ImageBitmap) {
        _recipeImage.value = pickedImage
    }

}