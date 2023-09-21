package com.diargegaj.recipesharing.data.models

data class RecipeDto(
    var recipeId: String = "",
    val title: String = "",
    val description: String = "",
    val ingredients: List<String> = listOf(""),
    val imageUrl: String = "",
    val userId: String = ""
)