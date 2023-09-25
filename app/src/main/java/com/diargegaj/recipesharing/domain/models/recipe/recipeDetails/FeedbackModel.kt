package com.diargegaj.recipesharing.domain.models.recipe.recipeDetails

import com.diargegaj.recipesharing.domain.models.UserModel

data class FeedbackModel(
    val feedbackId: String = "",
    val rating: Int = 5,
    val feedback: String = "",
    val recipeId: String = "",
    val userId: String = "",
    val userModel: UserModel? = null
)
