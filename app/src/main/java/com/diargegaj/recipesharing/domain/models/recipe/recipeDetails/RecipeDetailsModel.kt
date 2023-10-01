package com.diargegaj.recipesharing.domain.models.recipe.recipeDetails

import com.diargegaj.recipesharing.domain.models.UserModel

data class RecipeDetailsModel(
    var recipeId: String = "",
    var title: String = "",
    var description: String = "",
    var ingredients: List<String> = listOf(""),
    var imageUrl: String = "",
    var isPostedByLoggedUser: Boolean = false,
    val userModel: UserModel? = null,
    val averageRating: Int = 0,
    val myFeedbackModel: FeedbackModel? = null,
    val feedbacks: List<FeedbackModel> = listOf(),
)