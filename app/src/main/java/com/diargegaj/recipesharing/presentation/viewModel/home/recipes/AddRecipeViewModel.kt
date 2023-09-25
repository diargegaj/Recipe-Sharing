package com.diargegaj.recipesharing.presentation.viewModel.home.recipes

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.enums.ImagePath
import com.diargegaj.recipesharing.domain.models.recipe.RecipeModel
import com.diargegaj.recipesharing.domain.models.recipe.emptyRecipeModel
import com.diargegaj.recipesharing.domain.repository.ImageUploadRepository
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(
    private val imageUploadRepository: ImageUploadRepository,
    private val recipeRepository: RecipeRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val _recipeState = MutableStateFlow(emptyRecipeModel())
    val recipeState: StateFlow<RecipeModel> = _recipeState.asStateFlow()

    private val _recipeImage = MutableStateFlow<ImageBitmap?>(null)
    val recipeImage = _recipeImage.asStateFlow()

    private val _errorMessages = MutableSharedFlow<String>()
    val errorMessages: SharedFlow<String> get() = _errorMessages

    init {
        when (val result = userRepository.getUserId()) {
            is Resource.Success -> {
                _recipeState.value = _recipeState.value.copy(
                    userId = result.data
                )
            }

            else -> {

            }
        }
    }

    fun storeRecipe() {
        viewModelScope.launch {
            val recipeImage = _recipeImage.value ?: return@launch

            when (val result = imageUploadRepository.storeImage(
                recipeImage,
                ImagePath.RECIPE.getPath(UUID.randomUUID().toString())
            )) {
                is Resource.Success -> {
                    storeRecipeAdditionalInformation(result.data.toString())
                }

                is Resource.Error -> {
                    _errorMessages.emit("Failed to upload recipe image.")
                }

                else -> {}
            }

        }
    }

    private suspend fun storeRecipeAdditionalInformation(recipeImageUrl: String) {
        val recipeModel = _recipeState.value
        recipeModel.imageUrl = recipeImageUrl
        when (recipeRepository.storeRecipe(recipeModel)) {
            is Resource.Success -> {
                resetToDefaultState()
            }

            is Resource.Error -> {
                _errorMessages.emit("Failed to upload recipe additional information.")
            }

            else -> {

            }
        }
    }

    private fun resetToDefaultState() {
        val emptyState = emptyRecipeModel()
        emptyState.userId = _recipeState.value.userId
        _recipeState.value = emptyState
        _recipeImage.value = null
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

    fun updatePickedImage(pickedImage: ImageBitmap) {
        _recipeImage.value = pickedImage
    }

}