package com.diargegaj.recipesharing.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _state: MutableStateFlow<List<RecipeModel>> = MutableStateFlow(listOf())
    val state: StateFlow<List<RecipeModel>> = _state.asStateFlow()

    init {
        getRecipesFromCache()
        updateRecipesFromFirestore()
    }

    private fun getRecipesFromCache() {
        viewModelScope.launch {
            recipeRepository.observeAllRecipes().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = result.data
                    }

                    is Resource.Error -> {
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private fun updateRecipesFromFirestore() {
        viewModelScope.launch {
            when (recipeRepository.updateRecipesFromFirestore()) {
                is Resource.Success -> {

                }

                is Resource.Error -> {

                }

                else -> {

                }
            }

        }
    }


}

