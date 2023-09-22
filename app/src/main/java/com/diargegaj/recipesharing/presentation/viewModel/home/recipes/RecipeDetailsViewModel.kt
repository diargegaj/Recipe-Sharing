package com.diargegaj.recipesharing.presentation.viewModel.home.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.models.emptyRecipeModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _state: MutableStateFlow<RecipeModel> = MutableStateFlow(emptyRecipeModel())
    val state: StateFlow<RecipeModel> = _state.asStateFlow()

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> get() = _messages

    init {
        val recipeId: String =
            savedStateHandle["recipeId"] ?: throw IllegalArgumentException("RecipeId is required.")

        gerRecipeFromCache(recipeId)

    }

    private fun gerRecipeFromCache(recipeId: String) {
        viewModelScope.launch {
            recipeRepository.getRecipeDetailsWithId(recipeId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = result.data
                    }

                    is Resource.Error -> {
                        _messages.emit(
                            "Failed loading recipe details."
                        )
                    }

                    Resource.Loading -> Unit
                }
            }
        }
    }

}