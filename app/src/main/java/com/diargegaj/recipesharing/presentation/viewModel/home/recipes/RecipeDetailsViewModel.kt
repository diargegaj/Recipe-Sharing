package com.diargegaj.recipesharing.presentation.viewModel.home.recipes

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.FeedbackModel
import com.diargegaj.recipesharing.domain.models.recipe.recipeDetails.RecipeDetailsModel
import com.diargegaj.recipesharing.domain.repository.RecipeRepository
import com.diargegaj.recipesharing.domain.repository.userAuth.UserAuthRepository
import com.diargegaj.recipesharing.domain.repository.userProfile.UserProfileRepository
import com.diargegaj.recipesharing.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val userAuthRepository: UserAuthRepository,
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RecipeDetailsModel())
    val state = _state.asStateFlow()

    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> get() = _messages

    private var recipeId: String
    private var userId: String

    init {
        recipeId =
            savedStateHandle["recipeId"] ?: throw IllegalArgumentException("RecipeId is required.")
        userId = (userAuthRepository.getUserId() as Resource.Success).data

        getRecipeFromCache()
        loadFeedbacksFromCache()
        updateFeedbacks()
    }

    private fun loadFeedbacksFromCache() {
        viewModelScope.launch {
            recipeRepository.getFeedbacksPerRecipe(recipeId).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        if (result.exception !is NotFoundException) {
                            _messages.emit(
                                "Failed loading recipe reviews"
                            )
                        }
                    }

                    is Resource.Success -> {
                        handleSuccessfulFeedbackLoad(result.data)
                    }

                    else -> Unit
                }
            }
        }
    }

    private suspend fun handleSuccessfulFeedbackLoad(feedbacks: List<FeedbackModel>) {
        feedbacks.forEach { feedback ->
            processFeedback(feedback)
        }
    }

    private suspend fun processFeedback(feedback: FeedbackModel) {
        if (feedback.userModel == null) {
            fetchUserInfo(feedback.userId)
            return
        }

        if (feedback.userModel.userUUID == userId) {
            _state.value = _state.value.copy(myFeedbackModel = feedback)
        } else {
            val currentFeedbacks = _state.value.feedbacks.toMutableSet()
            currentFeedbacks.add(feedback)
            _state.value = _state.value.copy(feedbacks = currentFeedbacks.toList())
        }
    }

    private suspend fun fetchUserInfo(userId: String) {
        when (val result = userProfileRepository.getUserInfoFromFirestore(userId)) {
            is Resource.Success -> {
                userProfileRepository.saveUserInfoOnCache(result.data)
            }

            else -> Unit
        }
    }

    private fun updateFeedbacks() {
        viewModelScope.launch {
            when (val result = recipeRepository.updateFeedbacksPerRecipe(recipeId)) {
                is Resource.Error -> {
                    if (result.exception !is NotFoundException) {
                        _messages.emit(
                            "Failed to get latest recipe reviews"
                        )
                    }
                }

                else -> Unit
            }
        }
    }

    private fun getRecipeFromCache() {
        viewModelScope.launch {
            val loggedInUserId = when (val result = userAuthRepository.getUserId()) {
                is Resource.Success -> result.data
                else -> ""
            }
            recipeRepository.getRecipeDetailsWithId(recipeId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data.isPostedByLoggedUser =
                            loggedInUserId == result.data.userModel?.userUUID
                        _state.value = result.data
                    }

                    is Resource.Error -> {
                        _messages.emit(
                            "Failed loading recipe details."
                        )
                    }

                    else -> Unit
                }
            }
        }
    }

    fun deleteRecipe() {
        viewModelScope.launch {
            when (recipeRepository.deleteRecipe(recipeId)) {
                is Resource.Success -> {
                    _messages.emit(
                        "Recipe deleted successfully"
                    )
                }

                is Resource.Error -> {
                    _messages.emit(
                        "Failed to delete recipe"
                    )
                }

                else -> Unit
            }
        }
    }

    private var _currentRating = MutableStateFlow(1)
    val currentRating = _currentRating.asStateFlow()

    private var _feedbackText = MutableStateFlow("")
    val feedbackText = _feedbackText.asStateFlow()

    fun onFeedbackSubmit() {
        viewModelScope.launch {
            val results = recipeRepository.addFeedback(
                FeedbackModel(
                    rating = currentRating.value,
                    feedback = feedbackText.value,
                    userId = userId,
                    recipeId = recipeId
                )
            )

            when (results) {
                is Resource.Success -> {
                    _feedbackText.value = ""
                    _currentRating.value = 1

                    _messages.emit(
                        "Review added successfully"
                    )

                    updateFeedbacks()
                }

                is Resource.Error -> {
                    _messages.emit(
                        "Error adding review."
                    )
                }

                else -> Unit
            }
        }
    }

    fun onNewRating(newRating: Int) {
        _currentRating.value = newRating
    }

    fun onNewFeedbackValue(feedbackValue: String) {
        _feedbackText.value = feedbackValue
    }
}