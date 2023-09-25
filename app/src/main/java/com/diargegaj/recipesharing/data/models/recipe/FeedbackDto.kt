package com.diargegaj.recipesharing.data.models.recipe

data class FeedbackDto(
    var feedbackId: String = "",
    val rating: Int = 5,
    val feedback: String = "",
    val recipeId: String = "",
    val userId: String = ""
)