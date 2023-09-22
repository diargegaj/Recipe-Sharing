package com.diargegaj.recipesharing.domain.models

data class RecipeForViewModel(
    val recipeId: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val ingredients: List<String> = listOf(),
    val userId: String = "",
    val userModel: UserModel? = emptyUserModel()
)