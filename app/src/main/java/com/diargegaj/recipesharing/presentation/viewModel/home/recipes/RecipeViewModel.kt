package com.diargegaj.recipesharing.presentation.viewModel.home.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.data.mappers.toUiModel
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.models.RecipeUIModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state: MutableStateFlow<List<RecipeUIModel>> = MutableStateFlow(listOf())
    val state: StateFlow<List<RecipeUIModel>> = _state.asStateFlow()

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> get() = _messages

    init {
        getRecipesFromCache()
        updateRecipesFromFirestore()
    }

    private fun getRecipesFromCache() {
        viewModelScope.launch {
            recipeRepository.observeAllRecipes().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data.forEach {
                            fetchUserInfo(it)
                        }
                    }

                    is Resource.Error -> {
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private suspend fun fetchUserInfo(recipeModel: RecipeModel) {
        when (val result = userRepository.getUserInfoFromCache(recipeModel.userId).first()) {
            is Resource.Success -> {
                val recipeUIModel = recipeModel.toUiModel(userInfo = result.data)
                val currentRecipes = _state.value.toMutableSet()
                currentRecipes.add(recipeUIModel)
                _state.value = currentRecipes.toList()
            }

            else -> {
                fetchUserInfoFromFirestore(recipeModel)
            }
        }
    }

    private suspend fun fetchUserInfoFromFirestore(recipeModel: RecipeModel) {
        when (val result = userRepository.getUserInfoFromFirestore(recipeModel.userId)) {
            is Resource.Success -> {
                userRepository.saveUserInfoOnCache(result.data)
                val recipeUIModel = recipeModel.toUiModel(userInfo = result.data)
                _state.value = _state.value + recipeUIModel
            }

            else -> {

            }
        }
    }

    private fun updateRecipesFromFirestore() {
        viewModelScope.launch {
            when (recipeRepository.updateRecipesFromFirestore()) {
                is Resource.Success -> {
                    _messages.emit(
                        "Recipes updated successfully from server"
                    )
                }

                is Resource.Error -> {
                    _messages.emit(
                        "Failed to get latest recipes"
                    )
                }

                else -> {

                }
            }
        }
    }
}

