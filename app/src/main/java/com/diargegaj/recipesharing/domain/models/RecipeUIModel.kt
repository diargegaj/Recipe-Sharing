package com.diargegaj.recipesharing.domain.models

data class RecipeUIModel(
    val recipeId: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val userModel: UserModel
)