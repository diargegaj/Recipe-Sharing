package com.diargegaj.recipesharing.presentation.viewModel.home.recipes

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {

        val recipeId: String =
            savedStateHandle["recipeId"] ?: throw IllegalArgumentException("RecipeId is required.")
        Log.d("diari1", "recipeId = $recipeId")
    }

}