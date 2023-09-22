package com.diargegaj.recipesharing.domain.models

data class RecipeModel(
    var recipeId: String = "",
    var title: String,
    var description: String,
    var ingredients: List<String> = listOf(""),
    var imageUrl: String = "",
    var userId: String = "",
    val userModel: UserModel? = null
)

fun emptyRecipeModel() = RecipeModel(
    recipeId = "",
    title = "",
    description = "",
    ingredients = listOf(""),
    imageUrl = "",
    userId = "",
    userModel = emptyUserModel()
)
