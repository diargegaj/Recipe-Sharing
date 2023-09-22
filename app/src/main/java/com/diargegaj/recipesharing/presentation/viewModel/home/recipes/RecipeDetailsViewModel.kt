package com.diargegaj.recipesharing.presentation.viewModel.home.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.RecipeForViewModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _state: MutableStateFlow<RecipeForViewModel> = MutableStateFlow(RecipeForViewModel())
    val state: StateFlow<RecipeForViewModel> = _state.asStateFlow()

    init {
        val recipeId: String =
            savedStateHandle["recipeId"] ?: throw IllegalArgumentException("RecipeId is required.")

        gerRecipeFromCache(recipeId)

    }

    private fun gerRecipeFromCache(recipeId: String) {
        viewModelScope.launch {
            recipeRepository.getRecipeDetailsWithId(recipeId).collect { result ->
                when(result) {
                    is Resource.Success -> {
                        _state.value = result.data
                    }
                    is Resource.Error -> TODO()
                    Resource.Loading -> TODO()
                }
            }
        }
    }

}