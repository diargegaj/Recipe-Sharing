package com.diargegaj.recipesharing.presentation.viewModel.home.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.RecipeModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.repository.UserRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state: MutableStateFlow<List<RecipeModel>> = MutableStateFlow(listOf())
    val state: StateFlow<List<RecipeModel>> = _state.asStateFlow()

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> get() = _messages

    private var searchRecipesJob: Job? = null

    init {
        loadRecipesFromCache()
        updateRecipesFromFirestore()
    }

    fun onQuerySearchQueryChange(query: String) {
        searchRecipesJob?.cancel()
        searchRecipesJob = viewModelScope.launch {
            delay(300)
            loadRecipesFromCache(query)
        }
    }

    private fun loadRecipesFromCache(query: String = "%%") {
        viewModelScope.launch {
            _state.value = emptyList()
            recipeRepository.observeAllRecipes(query).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data.forEach {
                            if (it.userModel != null) {
                                val currentRecipes = _state.value.toMutableSet()
                                currentRecipes.add(it)
                                _state.value = currentRecipes.toList()
                            } else {
                                fetchUserInfo(it)
                            }
                        }
                    }

                    is Resource.Error -> {

                    }

                    else -> Unit
                }
            }
        }
    }

    private suspend fun fetchUserInfo(recipeModel: RecipeModel) {
        when (val result = userRepository.getUserInfoFromFirestore(recipeModel.userId)) {
            is Resource.Success -> {
                userRepository.saveUserInfoOnCache(result.data)
            }

            else -> Unit
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

                else -> Unit
            }
        }
    }
}

