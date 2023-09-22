package com.diargegaj.recipesharing.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.data.mappers.toUiModel
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.models.RecipeUIModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state: MutableStateFlow<List<RecipeUIModel>> = MutableStateFlow(listOf())
    val state: StateFlow<List<RecipeUIModel>> = _state.asStateFlow()

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
        when (val result = userRepository.getUserInfoFromCache(recipeModel.userId)) {
            is Resource.Success -> {
                val recipeUIModel = recipeModel.toUiModel(userInfo = result.data)
                _state.value = _state.value + recipeUIModel
            }

            else -> {
                fetchUserInfoFromFirestore(recipeModel)
            }
        }
    }

    private suspend fun fetchUserInfoFromFirestore(recipeModel: RecipeModel) {
        when (val result = userRepository.getUserInfo(recipeModel.userId)) {
            is Resource.Success -> {
                userRepository.saveUserInfoOnCache(result.data)
                val recipeUIModel = recipeModel.toUiModel(userInfo = result.data)
                _state.value = _state.value + recipeUIModel

            }

            is Resource.Error -> {

            }

            else -> {

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

