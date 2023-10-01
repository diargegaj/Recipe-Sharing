package com.diargegaj.recipesharing.presentation.viewModel.home.recipes.editRecipe

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.enums.ImagePath
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.RecipeDetailsModel
import com.diargegaj.recipesharing.domain.repository.ImageUploadRepository
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditRecipeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    private val _recipeState = MutableStateFlow(RecipeDetailsModel())
    val recipeState: StateFlow<RecipeDetailsModel> = _recipeState.asStateFlow()

    private val _recipeImage = MutableStateFlow<ImageBitmap?>(null)
    val recipeImage = _recipeImage.asStateFlow()

    private val _userMessageEvent = MutableSharedFlow<String>(replay = 0)
    val userMessageEvent = _userMessageEvent.asSharedFlow()

    var recipeId: String

    init {
        recipeId =
            savedStateHandle["recipeId"] ?: throw IllegalArgumentException("RecipeId is required.")

        getRecipeFromCache()
    }

    private fun getRecipeFromCache() {
        viewModelScope.launch {
            recipeRepository.getRecipeDetailsWithId(recipeId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _recipeState.value = result.data
                    }

                    is Resource.Error -> {
                        _userMessageEvent.emit(
                            "Failed loading recipe data."
                        )
                    }

                    else -> Unit
                }
            }
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            val recipeImage = _recipeImage.value

            if (recipeImage != null) {
                updateRecipeImage(recipeImage)
            } else {
                updateRecipe()
            }
        }
    }

    private suspend fun updateRecipeImage(recipeImage: ImageBitmap) {
        when (val result = imageUploadRepository.storeImage(
            recipeImage,
            ImagePath.RECIPE.getPath(UUID.randomUUID().toString())
        )) {
            is Resource.Success -> {
                _recipeState.value = _recipeState.value.copy(
                    imageUrl = result.data.toString()
                )
                updateRecipe()
            }

            is Resource.Error -> {
                _userMessageEvent.emit(
                    "Failed to update recipe. Please try again."
                )
            }

            else -> Unit
        }
    }

    private suspend fun updateRecipe() {
        val recipe = _recipeState.value
        when (recipeRepository.updateRecipe(recipe)) {
            is Resource.Success -> {
                _userMessageEvent.emit(
                    "Recipe updated successfully."
                )
            }

            is Resource.Error -> {
                _userMessageEvent.emit(
                    "Failed to update recipe. Please try again."
                )
            }

            else -> Unit
        }
    }

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

    fun deleteIngredient(index: Int) {
        val ingredients = _recipeState.value.ingredients.toMutableList()
        ingredients.removeAt(index)
        _recipeState.value = _recipeState.value.copy(
            ingredients = ingredients
        )
    }

    fun updatePickedImage(pickedImage: ImageBitmap) {
        _recipeImage.value = pickedImage
    }

}
