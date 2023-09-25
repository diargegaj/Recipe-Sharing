package com.diargegaj.recipesharing.domain.models.recipe.recipeDetails

import com.diargegaj.recipesharing.domain.models.UserModel

data class RecipeDetailsModel(
    var recipeId: String = "",
    var title: String = "",
    var description: String = "",
    var ingredients: List<String> = listOf(""),
    var imageUrl: String = "",
    val userModel: UserModel? = null,
    val feedbacks: List<FeedbackModel> = listOf(),
)